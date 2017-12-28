(ns humandb.gui.state.localstorage
  (:require
    [cljs.reader :refer [read-string]]))

(defn update-connection-urls! [url]
  (let [raw-urls (.. js/localStorage (getItem "urls"))
        new-urls (if raw-urls
                   (-> raw-urls
                       read-string
                       (conj url))
                   (set [url]))]
    (.. js/localStorage (setItem "urls" (pr-str new-urls)))))

(defn get-connection-urls []
  (let [raw-urls (.. js/localStorage (getItem "urls"))]
    (if raw-urls
      (read-string raw-urls)
      #{})))
