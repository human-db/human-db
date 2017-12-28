(defproject humandb.rest-api-server "0.0.1"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [http-kit "2.2.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [compojure "1.6.0"]
                 [ring-middleware-format "0.7.2"]
                 [ring-cors "0.1.11"]

                 [humandb.processors.yaml "0.4.0-1"]
                 [humandb.persistors.github "0.4.0-3"]
                 [humandb.core "0.4.0"]]
  
  :main humandb.rest-api-server.core)
