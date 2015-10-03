(defproject skyscraper-cache-mapdb "0.1.0"
  :description "A MapDB-based cache backend for Skyscraper."
  :license {:name "MIT", :url "https://github.com/nathell/skyscraper-cache-mapdb/blob/master/README.md#license"}
  :scm {:name "git", :url "https://github.com/nathell/skyscraper"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.mapdb/mapdb "2.0-beta8"]
                 [skyscraper "0.2.0"]])
