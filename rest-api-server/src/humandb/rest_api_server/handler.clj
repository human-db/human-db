(ns humandb.rest-api-server.handler
  (:require
    [compojure.core :refer [routes]]
    [compojure.handler]
    [ring.middleware.format :refer [wrap-restful-format]]
    [ring.middleware.cors :refer [wrap-cors]]
    [humandb.rest-api-server.routes :as api]))

(def app
  (routes
    (-> api/routes
        (wrap-restful-format :formats [:json :transit-json])
        (wrap-cors :access-control-allow-origin [#".*"]
                   :access-control-allow-methods [:get :put :post :delete])
        compojure.handler/api)))
