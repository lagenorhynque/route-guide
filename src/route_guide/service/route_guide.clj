(ns route-guide.service.route-guide
  (:require
   [clojure.core.async :as async]
   [io.grpc.examples.routeguide.RouteGuide.server :as route-guide]
   [ring.util.response :refer [response]]
   [route-guide.boundary.db.feature :as db.feature])
  (:import
   (java.time
    Duration
    Instant)))

(defn calculate-dinstance [start end]
  (let [coord-factor 10000000.0
        lat-1 (/ (:latitude start) coord-factor)
        lat-2 (/ (:latitude end) coord-factor)
        lon-1 (/ (:longitude start) coord-factor)
        lon-2 (/ (:longitude end) coord-factor)
        lat-rad-1 (Math/toRadians lat-1)
        lat-rad-2 (Math/toRadians lat-2)
        delta-lat-rad (Math/toRadians (- lat-2 lat-1))
        delta-lon-rad (Math/toRadians (- lon-2 lon-1))
        ;; Formula is based on http://mathforum.org/library/drmath/view/51879.html
        a (+ (Math/pow (Math/sin (/ delta-lat-rad 2)) 2)
             (* (Math/cos lat-rad-1)
                (Math/cos lat-rad-2)
                (Math/pow (Math/sin (/ delta-lon-rad 2)) 2)))
        c (* 2 (Math/atan2 (Math/sqrt a) (Math/sqrt (- 1 a))))
        r 6371000]
    (* r c)))

(defn summarize-points [db point-ch]
  (async/go-loop [point (async/<! point-ch)
                  prev-point nil
                  summary {:point-count 0
                           :feature-count 0
                           :distance 0.0}]
    (if point
      (recur (async/<! point-ch)
             point
             (cond-> summary
               true (update :point-count inc)
               (db.feature/find-feature-by-point db point) (update :feature-count inc)
               prev-point (update :distance + (calculate-dinstance prev-point point))))
      summary)))

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
                    :location point})))
     (ListFeatures [_ {:keys [db]
                       rectangle :grpc-params
                       out-ch :grpc-out}]
       (db.feature/find-features-within-rectangle db out-ch rectangle)
       (response out-ch))
     (RecordRoute [_ {:keys [db]
                      point-ch :grpc-params}]
       (let [start-time (Instant/now)
             summary (async/<!! (summarize-points db point-ch))
             elapsed-time (Duration/between start-time (Instant/now))]
         (response (-> summary
                       (update :distance long)
                       (assoc :elapsed-time (.getSeconds elapsed-time)))))))})
