(ns humandb.rest-api-server.core
  (:gen-class)
  (:require
    [org.httpkit.server :refer [run-server]]
    [humandb.rest-api-server.handler :refer [app]]))

(defonce server (atom nil))

(defn stop-server!
  []
  (println "Stopping server...")
  (@server :timeout 100)
  (reset! server nil))

(defn start-server!
  [port]
  (when @server
    (stop-server!))
  (reset! server
    (run-server #'app {:port port
                       :max-body (* 100 1024 1024)}))
  (println "Server started on" port))

(defn start! [port]
  (start-server! port))

(defn stop! []
  (stop-server!))

(defn -main [& args]
  (start! 5929))
