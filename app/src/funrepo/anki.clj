(ns funrepo.anki
  (:require [clj-http.client :as client]))

;; https://foosoft.net/projects/anki-connect/
(defonce anki-connect "localhost:8765")

(defn connect []
  (client/get anki-connect
              {:raw-args ["--retry-all-errors"
                          "--retry" "2"
                          "--retry-delay" "0"]}))

