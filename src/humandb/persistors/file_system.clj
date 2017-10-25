(ns humandb.persistors.file-system
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]
    [humandb.processor :as processor]
    [humandb.persistors.interface :as interface]))

(defn -record-file-path [db-config record-id]
  (str (db-config :data-path) "/" record-id "." (processor/extension db-config)))

(defmethod interface/read-record-ids :file-system
  [db-config]
  (->> (db-config :data-path)
       io/file
       file-seq
       (filter (fn [f]
                 (.isFile f)))
       (filter (fn [f]
                 (string/ends-with? (.getName f) (processor/extension db-config))))
       (map (fn [f]
              (second (re-matches (re-pattern (str "(.*)\\." (processor/extension db-config))) (.getName f)))))))

(defmethod interface/read-record :file-system
  [db-config record-id]
  (->> (-record-file-path db-config record-id)
       io/file
       slurp
       (processor/from-string db-config)))

(defmethod interface/write-record! :file-system
  [db-config record-id record-data]
  (spit (-record-file-path db-config record-id) 
        (processor/to-string db-config record-data)))
