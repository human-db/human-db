(ns human-db.persistor
  (:require
    [human-db.persistors.interface :as interface]
    [human-db.persistors.file-system]))

(def get-records interface/get-records)
(def read-record interface/read-record)
(def write-record! interface/write-record!)
