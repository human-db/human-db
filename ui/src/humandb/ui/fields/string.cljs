(ns humandb.ui.fields.string
  (:require
    [humandb.ui.fields.core :refer [field]]))

(defmethod field :string
  [{:keys [length value disabled on-change]}]
  (let [element (case length
                  :long :textarea
                  :short :input
                  :input)]
    [element {:value value
              :disabled disabled
              :on-change (fn [e]
                           (on-change (.. e -target -value)))}]))
