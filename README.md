# HumanDB


## Usage

```clojure

(ns project.foo
  (:require
    [humandb.core :as humandb]))

(def db-config
  {:adapter :yaml
   :data-path "data"
   :key-order [:id :title]})

; get all records
(humandb/get-records db-config)

; update key-value pair in a record
(humandb/update-record! db-config record-id {k v}) 

```

## Directory Spec

directory:

- contains 1 file per record

each file:

- name is id of record
- extension is based on chosen adapter
- contents are formatted according to the specified adapter
- contains data for 1 record
- keys within record are ordered alphabetically, descending
  - except for keys provided in :key-order which are given preference
- UUIDs are stored as strings


## Supported File Formats

 - yaml
