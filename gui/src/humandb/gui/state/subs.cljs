(ns humandb.gui.state.subs
  (:require
    [re-frame.core :refer [reg-sub]]))

(reg-sub :connected?
  (fn [db _]
    (db :connected?)))
