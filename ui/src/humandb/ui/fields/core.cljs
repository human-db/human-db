(ns humandb.ui.fields.core)

(defmulti field (fn [opts]
                  (opts :type)))
