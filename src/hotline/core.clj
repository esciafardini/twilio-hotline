(ns hotline.core
  (:require [clj-http.client :as client]
            [clojure.string :as str]
            [hotline.private :as p])
  (:gen-class))

(def message-url
  (str/join [p/base-url p/SID "/Messages"]))

;;CODE FOR FETCHING SMS RESPONSES
(defn get-msgs []
  (client/get
   (str message-url ".json")
   {:as :json
    :basic-auth [p/SID p/token]
    :query-params {:To p/twilio-number}}))

(def all-messages
  (:messages (:body (get-msgs))))

(defn phone-number->name [num]
  (get p/people num))

(defn msg-list
  "extracts [date] - [sender]: [message] and sorts ascending"
  []
  (->> (for [msg all-messages]
        (str (subs (:date_sent msg) 0 11) " - "
             (phone-number->name (:from msg)) ": "
             (:body msg)))
      reverse))

;;CODE FOR SENDING SMS
(defn send-sms [to body from]
  (try
    (client/post message-url
                 {:basic-auth [p/SID p/token]
                  :multipart [{:name "To" :content to}
                              {:name "From" :content from}
                              {:name "Body" :content body}]})
    (catch Exception e {:error e} (println e))))

(defn name->phone-number [name]
  (get p/phone-numbers name))
;;gross^

(def number-list
  (keys p/people))

(defn text-everybody [message]
  (run! #(send-sms % message p/twilio-number) number-list))

(defn text-somebody [name message]
  (send-sms (name->phone-number name) message p/twilio-number))

(defn -main
  [& args]
  ;;(text-everybody "I think its happening again")
  (text-somebody "Test" "TEST MESSAGE LOL"))




#_(defn formatted-msg-list
  "extracts [date] - [sender]: [message] and sorts ascending by date"
  []
  (reverse
   (sort-by key
            (reduce #(assoc %1
                            (subs (:date_sent %2) 0 25)
                            (str
                             (or
                              (phone-number->name (:from %2))
                              (:from %2)) ;;returns # if no name stored
                             ": "
                             (:body %2)))
                    {}
                    all-messages))))
