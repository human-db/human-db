(ns humandb.persistors.interface)

(defn get-persistor [db-config & args] 
  (get-in db-config [:persistor :type]))

(defmulti get-records get-persistor) 
(defmulti read-record get-persistor) 
(defmulti write-record! get-persistor) 

