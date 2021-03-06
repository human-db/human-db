(ns human-db.core
  (:require
    [human-db.persistor :as persistor]))

(defn get-records [db-config]
  (persistor/get-records db-config))

(defn get-record [db-config record-id]
  (persistor/read-record db-config record-id))

(defn update-record! [db-config record-id partial-record]
  (-> (or (persistor/read-record db-config record-id)
          {})
      (merge partial-record)
      (->> (persistor/write-record! db-config record-id))))

(defn store-record! [db-config record-id record]
  (persistor/write-record! db-config record-id record))
