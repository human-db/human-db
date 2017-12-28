(ns humandb.gui.state.events
  (:require
    [re-frame.core :refer [reg-event-fx reg-fx]]
    [humandb.gui.state.fx.ajax :as ajax]
    [humandb.gui.state.localstorage :as localstorage]))

(defn key-by-id [coll]
  (reduce (fn [memo item]
            (assoc memo (item :id) item)) {} coll))

(reg-fx :ajax ajax/ajax-fx)

(reg-event-fx :init
  (fn [_ _]
    {:db {:url nil
          :connected? false
          :connection-error nil
          :records {}
          :active-record-id nil}}))

(reg-event-fx :attempt-connect
  (fn [{db :db} [_ url]]
    {:db (assoc db :url url)
     :ajax {:uri (str url "/")
            :method :get
            :on-success :connect
            :on-error :show-connection-error}}))

(reg-event-fx :show-connection-error
  (fn [{db :db} _]
    {:db (-> db
             (assoc :connection-error (str "Unable to connect to \"" (db :url) "\""))
             (assoc :url nil))}))

(reg-event-fx :connect
  (fn [{db :db} _]
    (localstorage/update-connection-urls! (db :url))
    {:db (assoc db :connected? true)
     :ajax {:uri (str (db :url) "/records")
            :method :get
            :on-success :handle-records}}))

(reg-event-fx :handle-records
  (fn [{db :db} [_ records]]
    {:db (assoc db :records (key-by-id records))}))

(reg-event-fx :set-active-record
  (fn [{db :db} [_ id]]
    {:db (assoc db :active-record-id id)}))
