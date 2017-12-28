(ns humandb.gui.views.app-styles
  (:require
    [humandb.gui.views.pick-styles :refer [>pick]]
    [humandb.gui.views.db-styles :refer [>db]]))

(defn >app []
  [:>.app
   {:font-family "sans-serif"
    :line-height "1.5"}
   
   (>pick)
   
   (>db)])
