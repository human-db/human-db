(ns humandb.gui.views.app
  (:require
    [re-frame.core :refer [dispatch subscribe]]
    [humandb.gui.views.db :refer [db-view]]
    [humandb.gui.views.pick :refer [server-pick-view]]
    [humandb.gui.views.styles :refer [styles-view]]))

(defn app-view []
  [:div.app
   [styles-view]
   (if (not @(subscribe [:connected?]))
     [server-pick-view]
     [db-view])])
