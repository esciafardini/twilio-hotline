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

(defn filter-by-num
  "Filters p/people map based on matching phone number - returns a single entry"
  [num]
  (filter #(= (:phone (val %)) num) p/people))

(defn phone-number->name
  "Takes phone number as input, returns full name associated with it"
  [num]
  (-> num
       filter-by-num
       first         ;;returns first (only) entry from filtered list
       second        ;;returns the map containing :name and :phone
       :name))

(defn formatted-msg-list
  "extracts [date] - [sender]: [message] and sorts ascending by date"
  []
  (reverse (sort-by key
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

;;CODE FOR SENDING SMS
(defn send-sms [to body from]
  (try
    (client/post message-url
                 {:basic-auth [p/SID p/token]
                  :multipart [{:name "To" :content to}
                              {:name "From" :content from}
                              {:name "Body" :content body}]})
    (catch Exception e {:error e} (println e))))

(defn get-number [name]
  (get-in p/people [name :phone]))

(def number-list
  (reduce
   #(conj %1 (:phone (val %2)))
   '()
   p/people))

(defn text-everybody [message]
  (run! #(send-sms % message p/twilio-number) number-list))

(defn text-somebody [name message]
  (send-sms (get-number name) message p/twilio-number))

(defn -main
  [& args]
  ;;(text-everybody "I think its happening again")
  (text-somebody "test" "TEST MESSAGE LOL"))
