(ns humandb.persistors.interface)

(defn get-persistor [db-config & args] (db-config :persistor))

(defmulti read-record-ids get-persistor) 
(defmulti read-record get-persistor) 
(defmulti write-record! get-persistor) 

