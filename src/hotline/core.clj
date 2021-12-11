(ns hotline.core
  (:require [clj-http.client :as client]
            [clojure.string :as str]
            [hotline.private :as p])
  (:gen-class))

(def message-url
  (str/join [p/base-url p/SID "/Messages"]))

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
  (text-somebody "test" "testing testing testing.")
  )
