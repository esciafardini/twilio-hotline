(ns hotline.core
  (:require [clj-http.client :as client]
            [clojure.string :as str]
            [hotline.private :as p])
  (:gen-class))

(def message-url
  (str/join [p/base-url p/SID "/Messages"]))

(defn get-number [name]
  (get-in p/people [name :phone]))

(defn send-sms [to body from]
  (try
    (client/post message-url
                 {:basic-auth [p/SID p/token]
                  :multipart [{:name "To" :content to}
                              {:name "From" :content from}
                              {:name "Body" :content body}]})
    (catch Exception e {:error e} (println e))))

(def number-list (map :phone (map val p/people)))

(defn text-everybody [message]
  (run! #(send-sms % message p/twilio-number) number-list))

(defn text-somebody [name message]
  (send-sms (get-number name) message p/twilio-number))

(defn -main
  [& args]
  ;(text-everybody "HELLO?!")
  (text-somebody "test" "This text is a test")) ;;test is my phone #
