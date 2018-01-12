(ns humandb.ui.fields.datetime
  (:require
    [clojure.string :as string]
    [cljs-time.format :as f]
    [cljs-time.coerce :as c]
    [humandb.ui.fields.core :refer [field]]))

(defmethod field :datetime
  [{:keys [value on-change]}]
  [:input {:type "datetime-local"
           :value (when value
                    (f/unparse
                      (f/formatter "YYYY-MM-dd'T'HH:mm:ss")
                      (c/from-date value)))
           :on-change (fn [e]
                        (let [value (.. e -target -value)]
                          (cond
                            (string/blank? value)
                            (on-change nil)

                            (= 16 (count value))
                            (on-change (c/to-date
                                         (f/parse
                                           (f/formatter
                                             "YYYY-MM-dd'T'HH:mm")
                                           (.. e -target -value))))

                            (= 19 (count value))
                            (on-change (c/to-date
                                         (f/parse
                                           (f/formatter
                                             "YYYY-MM-dd'T'HH:mm:ss")
                                           (.. e -target -value)))))))}])
