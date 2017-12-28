(ns humandb.gui.views.db
  (:require
    [re-frame.core :refer [dispatch subscribe]]))

(defn records-view [records]
  [:table
   [:thead]
   [:tbody
    (for [record records]
      ^{:key (record :id)}
      [:tr
       [:td (record :id)]])]])

(defn db-view []
  [:div
   "Connected"
   [records-view @(subscribe [:records])]])
