(ns hotline.core
  (:require
   [clj-http.client :as client]
   [clojure.string :as str]
   [hotline.private :as p])
  (:gen-class))

(def tedmoon "\uD83C\uDF1D")

(def message-url
  (str/join [p/base-url p/SID "/Messages"]))

;;CODE FOR FETCHING SMS RESPONSES
(defn get-msgs
  ([]
   (get-msgs nil))
  ([specifier]
   (->>
    (client/get (str message-url ".json")
                {:as :json
                 :basic-auth [p/SID p/token]
                 :query-params (if
                                (= specifier :incoming)
                                 {:To p/twilio-number}
                                 nil)})
    :body
    :messages)))

(defn phone-number->name [num]
  (get p/people num))

(defn response->map-of-messages
  "extracts [date] - [sender]: [message] and sorts ascending"
  [messages-response]
  (->> (for [msg messages-response
             :let [date-sent (:date_sent msg)
                   sender-number (:from msg)
                   sender-name (phone-number->name (:from msg))
                   recip-name (phone-number->name (:to msg))
                   recip-number (:from msg)]]
         {:moon tedmoon
          :msg (:body msg)
          :from (or sender-name sender-number)
          :to (or recip-name recip-number)
          :date (subs date-sent 0 11)})
       reverse))

;;CODE FOR SENDING SMS
(defn send-sms 
  "Absolutely impure function, use with caution" 
  [to body from]
  (try
    (client/post message-url
                 {:basic-auth [p/SID p/token]
                  :multipart [{:name "To" :content to}
                              {:name "From" :content from}
                              {:name "Body" :content body}]})
    (catch Exception e {:error e} (println e))))

(defn name->phone-number 
  "Looks up phone numbers by name" 
  [name]
  (get p/phone-numbers name))

;;commented out to avoid accidental SMS sending
;;
#_(def number-list
    (keys p/people))

#_(defn text-everybody [message]
    (run! #(send-sms % message p/twilio-number) number-list))

(defn text-somebody 
  "It isn't pure and it shadows clojure.core `name` variable in the 
  function scope - BEWARE"  
  [name message]
  (send-sms (name->phone-number name) message p/twilio-number))

(defn -main
  []
  (response->map-of-messages (get-msgs :incoming)))

(comment
  (text-somebody "Test" (str "This is a test" tedmoon))
  (response->map-of-messages (get-msgs))
  (response->map-of-messages (get-msgs :incoming))
  )
