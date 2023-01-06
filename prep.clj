(ns user
  (:require [clojure.main :refer [demunge]]
            [babashka.process :refer [shell]]))

(defn fn-name [a-fn]
  (-> a-fn str demunge (str/split #"@") first (str/replace #"babashka." "")))

(defn does [act & args]
  (println (str "[prep] " (fn-name act) " " (reduce #(str %1 " " %2) args)))
  (apply act args))

(defn ask-os []
  (loop [bin nil]
    (or bin (recur (do (print "M1 macOS? [y/n]: ") (flush)
                       (case (read-line)
                         "y" "tailwindcss-macos-arm64"
                         "n" "tailwindcss-macos-x64"
                         false))))))

(try
  (does shell "./tailwindcss")
  (println "[bantu] tailwind ready!")
  (catch Exception _
    (println "no tailwind yet!")
    (let [tailwind-bin (ask-os)]
      (println "grabbing tailwind, putting in local folder...")
      (does shell (str "curl -sLO https://github.com/tailwindlabs/tailwindcss/releases/latest/download/" tailwind-bin))
      (does shell (str "mv -f " tailwind-bin " tailwindcss"))
      (does shell "chmod +x tailwindcss")
      (does shell "./tailwindcss")
      (println "[bantu] tailwind ready!"))))
