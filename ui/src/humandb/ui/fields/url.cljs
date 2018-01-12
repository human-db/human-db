(ns humandb.ui.fields.url
  (:require
    [humandb.ui.fields.core :refer [field]]))

(defmethod field :url
  [{:keys [value on-change]}]
  [:input {:type "url"
           :value value
           :on-change (fn [e]
                        (on-change (.. e -target -value)))}])
