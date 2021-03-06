(ns human-db.persistors.github
  (:require
    [clojure.data.json :as json]
    [clojure.string :as string]
    [me.raynes.fs :as fs]
    [org.httpkit.client :as http]
    [me.raynes.fs.compression :as fs.compression]
    [human-db.processor :as processor]
    [human-db.persistors.interface :as interface]
    [human-db.persistors.file-system :as file-system])
  (:import
    [org.apache.commons.codec.digest DigestUtils]
    [org.apache.commons.codec.binary Base64]))

(defn base64-encode [s]
  (-> s
      (.getBytes)
      (Base64/encodeBase64String))) 

(defn base64-decode [s]
  (-> s 
      (Base64/decodeBase64)
      (String.)))

(defn -git-sha [s]
  (DigestUtils/sha1Hex (str "blob " (count s) "\u0000" s)))

(defn -github-auth-headers [db-config]
  {"User-Agent" (get-in db-config [:persistor :user])
   "Authorization" (str "token " (get-in db-config [:persistor :token]))})

(defonce -filename->sha (atom {}))

(defn -fetch-file [db-config path]
  (let [repo (get-in db-config [:persistor :repo])
        branch (get-in db-config [:persistor :branch])
        file (-> @(http/get (str "https://api.github.com/repos/" repo "/contents/" path)
                            {:query-params {:ref branch}
                             :headers (-github-auth-headers db-config)})
                 :body
                 (json/read-str :key-fn keyword))]
    (swap! -filename->sha assoc (file :name) (file :sha))
    (some-> file
            :content
            (string/replace #"\n" "")
            base64-decode)))

(defn -update-file! [db-config path {:keys [content message]}]
  (let [repo (get-in db-config [:persistor :repo])
        branch (get-in db-config [:persistor :branch])
        author (get-in db-config [:persistor :author])
        committer (get-in db-config [:persistor :committer])
        filename (last (string/split path #"/"))
        response (-> @(http/put (str "https://api.github.com/repos/" repo "/contents/" path)
                                {:headers (merge (-github-auth-headers db-config)
                                                 {"Content-Type" "application/json"})
                                 :body (json/write-str
                                         {:branch branch
                                          :path path
                                          :message message
                                          :content (base64-encode content)
                                          :sha (@-filename->sha filename)
                                          :committer committer
                                          :author author})})
                     :body
                     (json/read-str :key-fn keyword))]
    (if (get-in response [:content :name])
      (do
        (swap! -filename->sha assoc
               (get-in response [:content :name])
               (get-in response [:content :sha]))
        true)
      (do
        (println "ERROR PUSHING FILE TO GITHUB" path response)
        nil))))

(defn -calc-shas [files]
  (->> files
       (remove (fn [f]
                 (.isDirectory f)))
       (reduce (fn [memo file]
                 (assoc memo (.getName file) (-git-sha (slurp file)))) {})))

(defn -fetch-archive! [db-config]
  (let [temp-file (fs/temp-file "human-db_data_archive")
        temp-dir (fs/temp-dir "human-db_data_archive_unpacked")
        repo (get-in db-config [:persistor :repo])
        branch (get-in db-config [:persistor :branch])]
    (clojure.java.io/copy 
      (:body @(http/get (str "https://api.github.com/repos/" repo "/zipball/" branch)
                        {:headers (-github-auth-headers db-config)
                         :as :byte-array}))
      temp-file)
    (fs.compression/unzip temp-file temp-dir)
    (let [data-path (get-in db-config [:persistor :data-path])
          data-dir (first (fs/find-files* (.getPath temp-dir) 
                                          (fn [f]
                                            (and (.isDirectory f)
                                                 (re-matches 
                                                   (re-pattern (str ".*" data-path))
                                                   (.getPath f))))))]
      (reset! -filename->sha (-calc-shas (file-seq data-dir)))
      data-dir)))

(defmethod interface/get-records :github
  [db-config]
  (->> (-fetch-archive! db-config)
       (file-system/-files db-config)
       (map (fn [f]
              (->> f
                  slurp
                  (processor/from-string db-config))))))

(defmethod interface/read-record :github
  [db-config record-id]
  (some->> (file-system/-record-file-path db-config record-id) 
           (-fetch-file db-config)
           (processor/from-string db-config)))

(defmethod interface/write-record! :github
  [db-config record-id record-data]
  (-update-file! db-config (file-system/-record-file-path db-config record-id)
                 {:content (processor/to-string db-config record-data)
                  :message (str "Update " record-id)}))
