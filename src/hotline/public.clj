(ns hotline.public
  (:require
   [clojure.set :as set]))

;;Added for reference
;;
(def SID "YOUR-TWILIO-SID")
(def token "YOUR-AUTH-TOKEN")
(def twilio-number "YOUR-TWILIO-PHONE-NUMBER") ;;in the format "+1555555555"
(def base-url "https://api.twilio.com/2010-04-01/Accounts/")

(def people {"+14105553333" "Test"
             "+17035553333" "Joe"
             "+16105553334" "James"
             "+16435553335" "Rocco"
             "+16105553336" "Jake"
             "+16435553337" "Sam"
             "+16015553338" "Paco"
             "+16105553339" "Rebekka"})

(def phone-numbers (set/map-invert people))
