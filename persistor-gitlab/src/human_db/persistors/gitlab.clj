(ns human-db.persistors.gitlab
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

(defn -auth-headers [db-config]
  {"PRIVATE-TOKEN" (get-in db-config [:persistor :token])})

(def api-base-url "https://gitlab.com/api/v4")

(defn -fetch-file [db-config path]
  (let [project-id (get-in db-config [:persistor :project-id])
        branch (get-in db-config [:persistor :branch])
        file (-> @(http/get (str api-base-url "/projects/" project-id "/repository/files/" (http/url-encode path))
                            {:query-params {:ref branch}
                             :headers (-auth-headers db-config)})
                 :body
                 (json/read-str :key-fn keyword))]
    (some-> file
            :content
            (string/replace #"\n" "")
            base64-decode)))

(defn -update-file! [db-config path {:keys [content message]}]
  (let [project-id (get-in db-config [:persistor :project-id])
        branch (get-in db-config [:persistor :branch])
        author (get-in db-config [:persistor :author])
        response (-> @(http/put (str api-base-url "/projects/" project-id "/repository/files/" (http/url-encode path))
                                {:headers (merge (-auth-headers db-config)
                                                 {"Content-Type" "application/json"})
                                 :body (json/write-str
                                         {:branch branch
                                          :commit_message message
                                          :encoding "base64"
                                          :content (base64-encode content)
                                          :author_email (author :email)
                                          :author_name (author :name)})}))]
    (if (= 200 (response :status))
      true
      (do
        (println "ERROR PUSHING FILE TO GITHUB" path (response :body))
        nil))))

(defn -fetch-archive! [db-config]
  (let [temp-file (fs/temp-file "human-db_data_archive" "zip")
        temp-dir (fs/temp-dir "human-db_data_archive_unpacked")
        project-id (get-in db-config [:persistor :project-id])
        branch (get-in db-config [:persistor :branch])]
    (clojure.java.io/copy
      (:body @(http/get (str api-base-url "/projects/" project-id "/repository/archive.zip")
                        {:headers (-auth-headers db-config)
                         :query-params {:sha branch}
                         :as :byte-array}))
      temp-file)
    (fs.compression/unzip temp-file temp-dir)
    (let [data-path (get-in db-config [:persistor :data-path])
          data-dir (first (fs/find-files* (.getPath temp-dir)
                                          (fn [f]
                                            (and (.isDirectory f)
                                                 (string/ends-with? (.getPath f) data-path)))))]
      data-dir)))

(defmethod interface/get-records :gitlab
  [db-config]
  (->> (-fetch-archive! db-config)
       (file-system/-files db-config)
       (map (fn [f]
              (->> f
                  slurp
                  (processor/from-string db-config))))))

(defmethod interface/read-record :gitlab
  [db-config record-id]
  (some->> (file-system/-record-file-path db-config record-id)
           (-fetch-file db-config)
           (processor/from-string db-config)))

(defmethod interface/write-record! :gitlab
  [db-config record-id record-data]
  (-update-file! db-config (file-system/-record-file-path db-config record-id)
                 {:content (processor/to-string db-config record-data)
                  :message (str "Update " record-id)}))


