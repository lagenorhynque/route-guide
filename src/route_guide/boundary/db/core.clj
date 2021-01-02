(ns route-guide.boundary.db.core
  (:require
   [cheshire.core :as cheshire]
   [clojure.java.io :as io]
   [integrant.core :as ig]))

(defrecord Boundary [data])

(defmethod ig/init-key ::db
  [_ {:keys [path]}]
  (-> (io/resource path)
      io/reader
      (cheshire.core/parse-stream-strict true)
      ->Boundary))
