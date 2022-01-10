(ns hotline.core
  (:require
   [clj-http.client :as client]
   [clojure.string :as str]
   [hotline.private :as p])
  (:gen-class))

(def message-url
  (str/join [p/base-url p/SID "/Messages"]))

;;CODE FOR FETCHING SMS RESPONSES
(defn get-incoming-messages []
  (client/get
   (str message-url ".json")
   {:as :json
    :basic-auth [p/SID p/token]
    :query-params {:To p/twilio-number}}))

(defn get-all-msgs []
  (client/get
   (str message-url ".json")
   {:as :json
    :basic-auth [p/SID p/token]}))

(defn phone-number->name [num]
  (get p/people num))

(def all-messages
  (-> (get-all-msgs)
      :body
      :messages))

(def incoming-messages
  (-> (get-incoming-messages)
      :body
      :messages))

(defn msg-list
  "extracts [date] - [sender]: [message] and sorts ascending"
  [messages-fn]
  (->> (for [msg messages-fn
             :let [date-sent (:date_sent msg)
                   sender-number (:from msg)
                   sender-name (phone-number->name (:from msg))
                   recip-name (phone-number->name (:to msg))
                   recip-number (:from msg)]]
         (str "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n\n"
              "Date: \t"
              (subs date-sent 0 11) " \n"
              "From: \t"
              (or sender-name sender-number) "\n"
              "To: \t"
              (or recip-name recip-number) "\n"
              "Msg: \t"
              (:body msg) "\n\n"))
       reverse
       println))

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

#_(def number-list
  (keys p/people))

#_(defn text-everybody [message]
    (run! #(send-sms % message p/twilio-number) number-list))

(defn text-somebody [name message]
  (send-sms (name->phone-number name) message p/twilio-number))

(defn -main
  []
  (msg-list all-messages))

(defn all []
  (msg-list all-messages))

(defn incoming []
  (msg-list incoming-messages))

(comment
  (text-somebody "Matt Uppy" "Im trying to get in touch with jonny craig?")

  (incoming)
  (all)

  )

