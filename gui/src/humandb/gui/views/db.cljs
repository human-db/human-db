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
       [:td {:on-click (fn [_]
                         (dispatch [:set-active-record (record :id)]))}
        (record :id)]])]])

(defn record-view [record]
  [:table
   [:thead]
   [:tbody
    (for [k (keys record)]
      ^{:key k}
      [:tr
       [:td k]
       [:td (str (record k))]])]])

(defn db-view []
  [:div.db
   [records-view @(subscribe [:records])]
   (when-let [record @(subscribe [:active-record])]
     [record-view record])])
