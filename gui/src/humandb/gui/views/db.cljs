(ns humandb.gui.views.db
  (:require
    [re-frame.core :refer [dispatch subscribe]]))

(defn record-view [record]
  [:div.record
   [:table
    [:thead]
    [:tbody
     (for [k (keys record)]
       ^{:key k}
       [:tr
        [:td.key k]
        [:td.value (str (record k))]])]]])

(defn search-bar-view []
  [:input.search-bar
   {:type "search"
    :placeholder "Search"
    :value @(subscribe [:search-query])
    :on-change (fn [e]
                 (dispatch [:update-search-query (.. e -target -value)]))}])

(defn search-results-view [ids]
  [:div.search-results
   (doall
     (for [id ids]
       ^{:key id}
       [record-view @(subscribe [:record id])]))])

(defn record-counter-view []
  [:div.record-counter
   (count @(subscribe [:search-result-ids])) " / "
   @(subscribe [:record-count]) " records"])
 
(defn db-view []
  [:div.db
   [:div.header
    [search-bar-view]
    [record-counter-view]]
   [search-results-view @(subscribe [:search-result-ids])]])
