(ns route-guide.service.route-guide
  (:require
   [io.grpc.examples.routeguide.RouteGuide.server :as route-guide]
   [ring.util.response :refer [response]]
   [route-guide.boundary.db.feature :as db.feature]))

(def service
  {:rpc-metadata (map #(assoc % :pkg "routeguide")
                      route-guide/rpc-metadata)
   :callback-context
   (reify route-guide/Service
     (GetFeature [_ {:keys [db]
                     point :grpc-params}]
       (if-let [feature (db.feature/find-feature-by-point db point)]
         (response feature)
         (response {:name ""
                    :location point}))))})
