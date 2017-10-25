(ns humandb.core
  (:require
    [humandb.persistor :as persistor]))

(defn get-records [db-config]
  (->> (persistor/read-record-ids db-config)
       (map (partial persistor/read-record db-config))))

(defn get-record [db-config record-id]
  (persistor/read-record db-config record-id))

(defn update-record! [db-config record-id partial-record]
  (-> (persistor/read-record db-config record-id)
      (merge partial-record)
      (->> (persistor/write-record! db-config record-id))))
