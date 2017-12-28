(ns humandb.gui.core
  (:require
    [reagent.core :as r]
    [re-frame.core :refer [dispatch-sync]]
    [humandb.gui.state.events]
    [humandb.gui.state.subs]
    [humandb.gui.views.app :refer [app-view]]))

(enable-console-print!)

(defn render []
  (r/render-component [app-view]
    (.. js/document (getElementById "app"))))

(defn ^:export init []
  (dispatch-sync [:init])
  (render))

(defn ^:export reload []
  (render))
