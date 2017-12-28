(ns humandb.gui.state.subs
  (:require
    [re-frame.core :refer [reg-sub]]
    [humandb.gui.state.localstorage :as localstorage]))

(reg-sub :connected?
  (fn [db _]
    (db :connected?)))

(reg-sub :records
  (fn [db _]
    (vals (db :records))))

(reg-sub :connection-urls
  (fn [db _]
    (localstorage/get-connection-urls)))

(reg-sub :connection-error
  (fn [db _]
    (db :connection-error)))
