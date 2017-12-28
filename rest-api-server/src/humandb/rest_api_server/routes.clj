(ns humandb.rest-api-server.routes
  (:require
    [compojure.core :refer [GET defroutes]]
    [humandb.processors.yaml]
    [humandb.core :as humandb]))

(def db-config
  {:processor :yaml
   :persistor {:type :file-system
               :data-path "../../../admin/bloom-notebook/data"}})

(defroutes routes

  (GET "/" _
    {:status 200
     :body {:status "OK"}})
  
  (GET "/records" _
    {:status 200 
     :body (humandb/get-records db-config)})
  
  (GET "/records/:id" [id] 
    {:status 200
     :body (humandb/get-record db-config id)}))

