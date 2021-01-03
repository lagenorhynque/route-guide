(ns route-guide.boundary.db.feature
  (:require
   [clojure.core.async :as async]
   [route-guide.boundary.db.core]))

(defprotocol Feature
  (find-feature-by-point [db point])
  (find-features-within-rectangle [db out-ch rectangle]))

(extend-protocol Feature
  route_guide.boundary.db.core.Boundary
  (find-feature-by-point [{:keys [data]} {:keys [latitude longitude]}]
    (some (fn [{:keys [location]
                :as feature}]
            (when (and (= (:latitude location) latitude)
                       (= (:longitude location) longitude))
              feature))
          data))
  (find-features-within-rectangle [{:keys [data]} out-ch {:keys [lo hi]}]
    (let [left (min (:longitude lo) (:longitude hi))
          right (max (:longitude lo) (:longitude hi))
          top (max (:latitude lo) (:latitude hi))
          bottom (min (:latitude lo) (:latitude hi))]
      (async/go
        (doseq [{:keys [location]
                 :as feature} data
                :when (and (<= left (:longitude location) right)
                           (<= bottom (:latitude location) top))]
          (async/>! out-ch feature))
        (async/close! out-ch)))))
