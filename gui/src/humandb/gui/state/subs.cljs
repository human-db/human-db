(ns humandb.gui.state.subs
  (:require
    [re-frame.core :refer [reg-sub]]
    [humandb.gui.state.localstorage :as localstorage]))

(reg-sub :connected?
  (fn [db _]
    (db :connected?)))

(reg-sub :connection-urls
  (fn [db _]
    (localstorage/get-connection-urls)))

(reg-sub :connection-error
  (fn [db _]
    (db :connection-error)))

(reg-sub :search-query
  (fn [db _]
    (db :search-query)))

(reg-sub :search-result-ids
  (fn [db _]
    (db :search-result-ids)))

(reg-sub :record
  (fn [db [_ id]]
    (get-in db [:records id])))

(reg-sub :record-count
  (fn [db _]
    (count (db :records))))
