
```clojure
(def db-config
  {:processor :yaml
   :persistor {:type :gitlab
               :data-path "data"
               :token "your-gitlab-api-token"
               :project-id "numeric-project-id"
               :branch "master"
               :author {:name "John Smith"
                        :email "john.smith@example.com"}}
   :key-order [:id]})
```
