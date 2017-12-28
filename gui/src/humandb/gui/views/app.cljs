(ns humandb.gui.views.app
  (:require
    [re-frame.core :refer [dispatch subscribe]]
    [humandb.gui.views.db :refer [db-view]]
    [humandb.gui.views.pick :refer [server-pick-view]]))

(defn app-view []
  (if (not @(subscribe [:connected?]))
    [server-pick-view]
    [db-view]))
