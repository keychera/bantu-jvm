(ns funrepo.anki
  (:require [clj-http.client :as client]
            [clojure.pprint :refer [pprint]]))

;; https://foosoft.net/projects/anki-connect/
(def anki-connect "http://localhost:8765")

(defn connect [] (client/get anki-connect))
