(ns humandb.processors.interface)

(defn get-processor [db-config & args] (db-config :processor))

(defmulti extension get-processor) 
(defmulti many-from-string get-processor)
(defmulti from-string get-processor)
(defmulti many-to-string get-processor)
(defmulti to-string get-processor)
