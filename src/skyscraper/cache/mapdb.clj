(ns skyscraper.cache.mapdb
  (:require [skyscraper.cache :refer [CacheBackend]]
            [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import [org.mapdb DB DBMaker Serializer]))

(defn edn-serializer []
  (proxy [Serializer] []
    (serialize [out obj]
      (binding [*print-length* nil *print-level* nil]
        (.writeUTF out (pr-str obj))))
    (deserialize [in available]
      (edn/read-string (.readUTF in)))))

(deftype MapDBCache
    [db strings-store values-store]
  CacheBackend
  (save-string [cache key string]
    (.put strings-store key string)
    (.commit db))
  (save [cache key value]
    (.put values-store key value)
    (.commit db))
  (load-string [cache key]
    (.get strings-store key))
  (load [cache key]
    (.get values-store key)))

(defn mapdb-cache
  [filename]
  (let [db (-> filename io/file DBMaker/newFileDB .closeOnJvmShutdown .make)
        strings (-> db
                    (.treeMapCreate "strings")
                    (.keySerializer Serializer/STRING)
                    (.valueSerializer Serializer/STRING)
                    .makeOrGet)
        values (-> db
                   (.treeMapCreate "values")
                   (.keySerializer Serializer/STRING)
                   (.valueSerializer (edn-serializer))
                   .makeOrGet)]
    (MapDBCache. db strings values)))

(defn close
  [^MapDBCache cache]
  (.close (.db cache)))

(defn export-values
  [^MapDBCache cache filename]
  (with-open [f (io/writer filename)]
    (binding [*print-length* nil, *print-level* nil, *out* f]
      (doseq [entry (-> cache .values-store .entrySet)]
        (prn [(key entry) (val entry)])))))

(defn import-values
  [^MapDBCache cache filename]
  (with-open [f (java.io.PushbackReader. (io/reader filename))]
    (binding [*print-length* nil, *print-level* nil]
      (loop [entry (edn/read {:eof nil} f)]
        (if entry
          (do
            (.put (.values-store cache) (first entry) (second entry))
            (recur (edn/read {:eof nil} f)))
          (.commit (.db cache)))))))
