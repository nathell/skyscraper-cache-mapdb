(ns skyscraper.cache.mapdb
  (:require [skyscraper.cache :refer [CacheBackend]]
            [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import [org.mapdb DB DBMaker Serializer]))

(defn edn-serializer []
  (proxy [Serializer] []
    (serialize [out obj]
      (.writeUTF out (pr-str obj)))
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
