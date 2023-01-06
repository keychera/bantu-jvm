(ns bantu.bantu
  (:require [clojure.core.match :refer [match]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [funrepo.anki :as anki]
            [selmer.parser :refer [render-file]]))

;; components
(defn app [main opts]
  (render-file "root.html" (merge {:title "bantu" :render-main main} opts)))

(defn connect-anki []
  (let [anki-response (try (anki/connect) (catch Exception e (println "[anki]" (.getMessage e)) nil))] 
    (if anki-response
      (render-file "connect-success.html" {:anki-connect-ver (:body anki-response)})
      (render-file "connect-failed.html" {}))))

(defn ^{:sidebar "hello" :title "Hello!"}
  hello [] "hello")

(defn ^{:sidebar "doc" :title "Doc"}
  doc [] (render-file "doc.html" {}))

(defn ^{:sidebar "anki" :title "Anki"}
  anki [] (render-file "anki.html" {}))

;; engine
(defn render-sidebars [selected]
  (->> [#'hello #'doc #'anki]
       (map (fn [handler]
              (let [{:keys [sidebar title icon]} (meta handler)]
                (render-file "sidebar.html" {:url sidebar :title title :selected (= sidebar selected)}))))
       (reduce str)))

(defn part? [req] (= "p" (:query-string req)))

(defn sidebar-route [partial? handler & args]
  (if partial? {:body (apply handler args)}
      (let [{:keys [sidebar]} (meta handler)]
        {:body (app (apply handler args) {:render-sidebars (render-sidebars sidebar)})})))

;; https://gist.github.com/borkdude/1627f39d072ea05557a324faf5054cf3
(defn router [req]
  (let [paths (vec (rest (str/split (:uri req) #"/")))
        verb (:request-method req)]
    (match [verb paths]
      ;; get
      [:get []] {:body (app nil {:render-sidebars (render-sidebars nil)})}
      [:get ["hello"]] (sidebar-route (part? req) #'hello)
      [:get ["doc"]] (sidebar-route (part? req) #'doc)
      [:get ["anki"]] (sidebar-route (part? req) #'anki)
      ;; post
      [:post ["connect-anki"]] {:body (connect-anki)}
      ;; resources
      [:get ["css" "style.css"]] {:headers {"Content-Type" "text/css"}
                                  :body (slurp (io/resource "public/css/style.css"))}
      :else {:status 404 :body "<p>Page not found.</p>"})))
