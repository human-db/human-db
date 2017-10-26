
```clojure
(def db-config
  {:processor :yaml
   :persistor {:type :github
               :data-path "data"
               :user "your-github-api-username"
               :token "your-github-api-token"
               :repo "owner/reponame"
               :branch "master"
               :author {:name "John Smith" 
                        :email "john.smith@example.com"}
               :committer {:name "Jane Doe"
                           :email "jane.doe@example.com"}}
   :key-order [:id]})
```
