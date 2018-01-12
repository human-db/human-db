(ns humandb.ui.fields.integer
  (:require
    [humandb.ui.fields.core :refer [field]]))

(defmethod field :integer
  [{:keys [value disabled on-change]}]
  [:input {:type "number"
           :disabled disabled
           :value value
           :on-change (fn [e]
                        (on-change (js/parseInt (.. e -target -value) 10)))}])
