(ns humandb.persistor
  (:require
    [humandb.persistors.interface :as interface]
    [humandb.persistors.file-system]))

(def read-record-ids interface/read-record-ids)
(def read-record interface/read-record)
(def write-record! interface/write-record!)
