(ns humandb.ui.fields.email
  (:require
    [humandb.ui.fields.core :refer [field]]))

(defmethod field :email
  [{:keys [value on-change]}]
  [:input {:type "email"
           :value value
           :on-change (fn [e]
                        (on-change (.. e -target -value)))}])
