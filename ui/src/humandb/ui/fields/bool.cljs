(ns humandb.ui.fields.bool
  (:require
    [humandb.ui.fields.core :refer [field]]))

(defmethod field :boolean
  [{:keys [value disabled on-change] :as config}]
  [:input {:type "checkbox"
           :checked (true? value)
           :on-change (fn [x]
                        (on-change (not value)))}])
