(ns serve
  (:require [ring.adapter.jetty9 :refer [run-jetty]]
            [bantu.bantu :as bantu]))

(defonce port 4242)
(defonce url (str "http://localhost:" port "/"))

(defn -main [& _]
  (println "[panas] starting...")
  (run-jetty #'bantu/router {:port port :join? false})
  (println "[panas] serving" url)
  @(promise))
