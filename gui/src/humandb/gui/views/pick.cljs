(ns humandb.gui.views.pick
  (:require
    [reagent.core :as r]
    [re-frame.core :refer [dispatch subscribe]]))

(defn server-connect-view []
  (let [url (r/atom "")]
    (fn []
      [:form {:on-submit (fn [e]
                           (.preventDefault e)
                           (dispatch [:attempt-connect @url]))}
       [:input {:type "text"
                :value @url
                :on-change (fn [e]
                             (reset! url (.. e -target -value)))}]
       [:button "Connect"]])))

(defn server-pick-view []
  [:div
   "Select a HumanDB server to connect to:"
   (for [url @(subscribe [:connection-urls])]
     ^{:key url}
     [:button 
      {:on-click (fn [_]
                   (dispatch [:attempt-connect url]))} 
      url])
   [server-connect-view]
   (when-let [error @(subscribe [:connection-error])]
     [:div error])])
