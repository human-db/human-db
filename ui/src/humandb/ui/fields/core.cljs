(ns humandb.ui.fields.core)

(defmulti field (fn [opts]
                  (opts :type)))

(defmethod field :default
  [entity k _]
  [:div "UNKNOWN FIELD TYPE"])
