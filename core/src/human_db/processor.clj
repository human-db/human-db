(ns human-db.processor
  (:require
    [human-db.processors.interface :as interface]))

(def extension interface/extension)
(def many-from-string interface/many-from-string)
(def from-string interface/from-string)
(def many-to-string interface/many-to-string)
(def to-string interface/to-string)
