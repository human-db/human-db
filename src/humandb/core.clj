(ns humandb.core
  (:require
    [humandb.io :as io]))

(defn get-records [db-config]
  (->> (io/read-record-ids db-config)
       (map (partial io/read-record db-config))))

(defn get-record [db-config record-id]
  (io/read-record db-config record-id))

(defn update-record! [db-config record-id partial-record]
  (-> (io/read-record db-config record-id)
      (merge partial-record)
      (->> (io/write-record! db-config record-id))))
