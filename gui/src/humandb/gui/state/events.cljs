(ns humandb.gui.state.events
  (:require
    [re-frame.core :refer [reg-event-fx reg-fx]]
    [humandb.gui.state.fx.ajax :as ajax]))

(reg-fx :ajax ajax/ajax-fx)

(reg-event-fx :init
  (fn [_ _]
    {:db {:url nil
          :connected? false}}))

(reg-event-fx :connect
  (fn [{db :db} [_ url]]
    {:db (assoc db :url url)
     :ajax {:uri (str url "/")
            :method :get
            :on-success :handle-initial-data}}))

(reg-event-fx :handle-initial-data
  (fn [{db :db} [_ data]]
    {:db (assoc db :connected? true)}))
