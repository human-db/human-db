(ns humandb.processors.yaml
  (:require
    [humandb.processors.interface :as interface]
    [yaml.writer]
    [yaml.reader]))

(def ^:dynamic entity-key-order [])

(defn- key-compare [key1 key2]
  (let [key1 (keyword key1)
        key2 (keyword key2)
        order entity-key-order]
    (cond
      ; key1 and key2 in order
      (and (not= -1 (.indexOf order key1))
           (not= -1 (.indexOf order key2)))
      (compare
        (.indexOf order key1)
        (.indexOf order key2))

      ; key1 in order
      (not= -1 (.indexOf order key1))
      -1

      ; key2 in order
      (not= -1 (.indexOf order key2))
      1

      ; neither key in order
      :else
      (compare key1 key2))))

(extend-protocol yaml.reader/YAMLReader
  java.lang.String
  (yaml.reader/decode [data]
    (if (= 36 (count data))
      (try
        (java.util.UUID/fromString data)
        (catch java.lang.IllegalArgumentException e
          data))
      data)))

(defn- into-sorted-map [data]
  (into (sorted-map-by key-compare)
        (->> data
             (remove (fn [[k v]] (nil? v)))
             (map (fn [[k v]]
                    [(yaml.writer/encode k) (yaml.writer/encode v)])))))

(extend-protocol yaml.writer/YAMLWriter
  java.util.UUID
  (yaml.writer/encode [data]
    (yaml.writer/encode (str data)))

  clojure.lang.PersistentVector
  (yaml.writer/encode [data]
    (vec (map yaml.writer/encode data)))

  clojure.lang.PersistentHashMap
  (yaml.writer/encode [data]
    (into-sorted-map data))

  flatland.ordered.map.OrderedMap
  (yaml.writer/encode [data]
    (into-sorted-map data))

  clojure.lang.PersistentArrayMap
  (yaml.writer/encode [data]
    (into-sorted-map data)))

(defmethod interface/extension :yaml [_] "yaml")

(defmethod interface/many-from-string :yaml
  [db-config s]
  (->> (yaml.reader/parse-documents s)
       ; yaml reader returns ordered-maps; convert to regular maps
       (map (partial into {}))))

(defmethod interface/from-string :yaml
  [db-config s]
  ; yaml reader returns ordered-maps; convert to regular maps
  (into {} (yaml.reader/parse-string s)))

(defmethod interface/many-to-string :yaml
  [db-config entities]
  (binding [entity-key-order (or (db-config :key-order) [])] 
    (->> entities
         (sort-by :id)
         (map (fn [entity]
                (yaml.writer/generate-string entity :dumper-options {:flow-style :block})))
         (clojure.string/join "---\n")
         (str "---\n"))))

(defmethod interface/to-string :yaml
  [db-config entity]
  (binding [entity-key-order (or (db-config :key-order) [])] 
    (yaml.writer/generate-string entity :dumper-options {:flow-style :block})))
