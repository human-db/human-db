(defproject humandb.gui "0.0.1"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [re-frame "0.10.2"]
                 [cljs-ajax "0.7.3"]]
  
  :plugins [[lein-figwheel "0.5.14"]]

  :main humandb.gui.server.core

  :clean-targets ^{:protect false}
  ["resources/public/js"]

  :figwheel {:server-port 1952}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/humandb/gui"]
                        :figwheel {:on-jsload "humandb.gui.core/reload"}
                        :compiler {:main "humandb.gui.core"
                                   :asset-path "/js/out"
                                   :output-to "resources/public/js/humandb.js"
                                   :output-dir "resources/public/js/out"}}]})
