(ns route-guide.grpc
  (:require
   [integrant.core :as ig]
   [io.pedestal.http :as http]
   [io.pedestal.http.body-params :as body-params]
   [protojure.pedestal.core :as protojure.pedestal]
   [protojure.pedestal.routes :as proutes]
   [route-guide.interceptor :as interceptor]
   [route-guide.service.route-guide :as route-guide]))

(defmethod ig/init-key ::routes
  [_ {:keys [db]}]
  (let [common-interceptors [(body-params/body-params)
                             (interceptor/attach-db db)]]
    (-> route-guide/service
        (assoc :interceptors common-interceptors)
        proutes/->tablesyntax
        set)))

(defmethod ig/init-key ::service
  [_ {:keys [options]}]
  (assoc options
         ::http/type protojure.pedestal/config
         ::http/chain-provider protojure.pedestal/provider))
