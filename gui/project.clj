(defproject humandb.gui "0.0.1"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 
                 [org.clojure/clojurescript "1.9.946"]
                 [re-frame "0.10.2"]]
  
  :plugins [[lein-figwheel "0.5.14"]]

  :figwheel {:server-port 1952}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/humandb/gui/client"]
                        :figwheel {:on-jsload "humandb.gui.client.core/reload"}
                        :compiler {:main "humandb.gui.client.core"
                                   :asset-path "/js/out"
                                   :output-to "resources/public/js/humandb.js"
                                   :output-dir "resources/public/js/out"}}]})
