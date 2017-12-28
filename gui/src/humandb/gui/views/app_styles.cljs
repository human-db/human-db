(ns humandb.gui.views.app-styles
  (:require
    [humandb.gui.views.pick-styles :refer [>pick]]))

(defn >app []
  [:>.app
   {:line-height "1.5"}
   
   (>pick)])
