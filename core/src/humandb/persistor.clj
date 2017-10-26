(ns humandb.persistor
  (:require
    [humandb.persistors.interface :as interface]
    [humandb.persistors.file-system]))

(def get-records interface/get-records)
(def read-record interface/read-record)
(def write-record! interface/write-record!)
