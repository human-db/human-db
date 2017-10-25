(ns humandb.test.helpers
  (:require
    [me.raynes.fs :as fs]))

(defmacro with-dir 
  [[dir-binding files] & body]
  `(let [~dir-binding (fs/temp-dir "humandb_test")]
     (doseq [[path# data#] ~files]
       (spit (fs/file (.getPath ~dir-binding) path#) data#))
     (let [result# (do ~@body)]
       (fs/delete-dir ~dir-binding)
       result#)))


