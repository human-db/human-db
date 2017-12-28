(ns humandb.gui.views.app
  (:require
    [reagent.core :as r]
    [re-frame.core :refer [dispatch subscribe]]))

(defn db-view []
  [:div
   "Connected"])

(defn server-pick-view []
  (let [url (r/atom "")]
    (fn []
      [:form {:on-submit (fn [e]
                           (.preventDefault e)
(dispatch [:connect @url]))}
       "Enter the URL of a HumanDB server to connect to:"
       [:input {:type "text"
                :value @url
                :on-change (fn [e]
                             (reset! url (.. e -target -value)))}]
       [:button "Connect"]])))

(defn app-view []
  (if (not @(subscribe [:connected?]))
    [server-pick-view]
    [db-view]))
