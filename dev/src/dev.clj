(ns dev
  (:refer-clojure :exclude [test])
  (:require
   [clojure.java.io :as io]
   [clojure.repl :refer :all]
   [clojure.tools.namespace.repl :refer [refresh]]
   [duct.core :as duct]
   [duct.core.repl :as duct-repl]
   [eftest.runner :as eftest]
   [fipp.edn :refer [pprint]]
   [integrant.core :as ig]
   [integrant.repl :refer [clear go halt init prep reset]]
   [integrant.repl.state :refer [config system]]))

(duct/load-hierarchy)

(defn read-config []
  (duct/read-config (io/resource "route_guide/config.edn")))

(defn test []
  (eftest/run-tests (eftest/find-tests "test")))

(def profiles
  [:duct.profile/dev :duct.profile/local])

(clojure.tools.namespace.repl/set-refresh-dirs "dev/src" "src" "test")

(when (io/resource "local.clj")
  (load "local"))

(integrant.repl/set-prep! #(duct/prep-config (read-config) profiles))
