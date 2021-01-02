(ns route-guide.boundary.db.feature
  (:require
   [route-guide.boundary.db.core]))

(defprotocol Feature
  (find-feature-by-point [db point]))

(extend-protocol Feature
  route_guide.boundary.db.core.Boundary
  (find-feature-by-point [{:keys [data]} {:keys [latitude longitude]}]
    (some (fn [{:keys [location]
                :as feature}]
            (when (and (= (:latitude location) latitude)
                       (= (:longitude location) longitude))
              feature))
          data)))
