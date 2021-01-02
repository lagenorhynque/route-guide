(ns route-guide.service.greeter
  (:require
   [com.example.addressbook.Greeter.server :as greeter]
   [ring.util.response :refer [response]]))

(def service
  {:rpc-metadata greeter/rpc-metadata
   :callback-context
   (reify greeter/Service
     (Hello [_ {{:keys [name]} :grpc-params}]
       (response {:message (str "Hello, " name)})))})
