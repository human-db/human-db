(ns humandb.gui.views.pick
  (:require
    [reagent.core :as r]
    [re-frame.core :refer [dispatch subscribe]]))

(defn server-connect-view []
  (let [url (r/atom "")]
    (fn []
      [:form
       {:on-submit (fn [e]
                     (.preventDefault e)
                     (dispatch [:attempt-connect @url]))}
       [:input {:type "text"
                :value @url
                :on-change (fn [e]
                             (reset! url (.. e -target -value)))}]
       [:button "Connect"]])))

(defn server-pick-view []
  [:div.pick
   [:div
    [:h1 "HumanDB"]
    [:h2 "Select a server to connect to:"]
    [:div.servers
     (for [url @(subscribe [:connection-urls])]
       ^{:key url}
       [:div.server 
        {:on-click (fn [_]
                     (dispatch [:attempt-connect url]))} 
        url])]
    "Or, connect to a new server:"
    [server-connect-view]
    (when-let [error @(subscribe [:connection-error])]
      [:div error])]])
