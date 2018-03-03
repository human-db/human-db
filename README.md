# Human DB


## Usage

```clojure
[human-db/core "0.5.0"]

; processors
[human-db/processors.yaml "0.5.0"]

; persistors
; file-system include in core
[human-db/persistors.github "0.5.0"]  
```

```clojure

(ns project.foo
  (:require
    [human-db.core :as human-db]
    [human-db.processors.yaml))

(def db-config
  {:processor :yaml
   :persistor {:type :file-system
               :data-path "data"}
   :key-order [:id :title]})

; get all records
(human-db/get-records db-config)

; update key-value pair in a record
(human-db/update-record! db-config record-id {k v}) 

```

## Directory Spec

directory:

- contains 1 file per record

each file:

- name is id of record
- extension is based on chosen processor
- contents are formatted according to the specified processor
- contains data for 1 record
- keys within record are ordered alphabetically, descending
  - except for keys provided in :key-order which are given preference
- UUIDs are stored as strings

