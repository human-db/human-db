(ns humandb.adapter
  (:require
    [humandb.adapters.yaml :as yaml]))

(defn many-from-string [db-config & args]
  (case (db-config :adapter)
    :yaml (apply yaml/many-from-string db-config args)))

(defn from-string [db-config & args]
  (case (db-config :adapter)
    :yaml (apply yaml/from-string db-config args)))

(defn many-to-string [db-config & args]
  (case (db-config :adapter)
    :yaml (apply yaml/many-to-string db-config args)))

(defn to-string [db-config & args]
  (case (db-config :adapter)
    :yaml (apply yaml/to-string db-config args)))

(defn extension [db-config]
  (case (db-config :adapter)
    :yaml (yaml/extension db-config)))


