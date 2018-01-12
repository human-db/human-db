(ns humandb.ui.fields.date
  (:require
    [cljs-time.format :as f]
    [cljs-time.coerce :as c]
    [humandb.ui.fields.core :refer [field]]))

(defmethod field :date
  [{:keys [value on-change]}]
  [:input {:type "date"
           :value (when value
                    (f/unparse
                      (f/formatter "yyyy-MM-dd")
                      (c/from-date value)))
           :on-change (fn [e]
                        (on-change (c/to-date
                                     (f/parse
                                       (f/formatter "yyyy-MM-dd")
                                       (.. e -target -value)))))}])
