(ns humandb.ui.fields.enumerable
  (:require
    [humandb.ui.fields.core :refer [field]]))

(defmethod field :enum
  [{:keys [value disabled options on-change]}]
  [:select {:value value
            :disabled disabled
            :on-change (fn [e]
                         (on-change (.. e -target -value)))}
   (for [option options]
     ^{:key (str option)}
     [:option {:value option} (str option)])])
