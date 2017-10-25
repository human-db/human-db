(ns humandb.io
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]
    [humandb.processor :as processor]))

(defn -record-file-path [db-config record-id]
  (str (db-config :data-path) "/" record-id "." (processor/extension db-config)))

(defn read-record-ids [db-config]
  (->> (db-config :data-path)
       io/file
       file-seq
       (filter (fn [f]
                 (.isFile f)))
       (filter (fn [f]
                 (string/ends-with? (.getName f) (processor/extension db-config))))
       (map (fn [f]
              (second (re-matches (re-pattern (str "(.*)\\." (processor/extension db-config))) (.getName f)))))))

(defn read-record [db-config record-id]
  (->> (-record-file-path db-config record-id)
       io/file
       slurp
       (processor/from-string db-config)))

(defn write-record! [db-config record-id record-data]
  (spit (-record-file-path db-config record-id) 
        (processor/to-string db-config record-data)))
