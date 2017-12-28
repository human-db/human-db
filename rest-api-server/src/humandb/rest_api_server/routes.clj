(ns humandb.rest-api-server.routes
  (:require
    [compojure.core :refer [GET defroutes]]))

(defroutes routes

  (GET "/" _
    {:status 200
     :body {:status "OK"}}))

