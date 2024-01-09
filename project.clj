(defproject route-guide "0.1.0"
  :description "Route Guide, an example gRPC API"
  :url "https://github.com/lagenorhynque/route-guide"
  :min-lein-version "2.8.1"
  :dependencies [[com.google.protobuf/protobuf-java "3.25.1"]
                 [duct.module.cambium "1.3.1" :exclusions [cheshire]]
                 [duct.module.pedestal "2.2.0" :exclusions [org.eclipse.jetty/jetty-http
                                                            org.eclipse.jetty/jetty-io
                                                            org.eclipse.jetty/jetty-util]]
                 [duct/core "0.8.0"]
                 [integrant "0.8.0"]
                 [io.undertow/undertow-core "2.3.10.Final"]
                 [io.undertow/undertow-servlet "2.3.10.Final"]
                 [org.clojure/clojure "1.11.1"]
                 [org.slf4j/slf4j-api "2.0.11"]
                 [protojure "1.7.3" :exclusions [io.pedestal/pedestal.log
                                                 org.clojure/core.async]]
                 [protojure/google.protobuf "1.0.0"]]
  :plugins [[duct/lein-duct "0.12.3"]]
  :main ^:skip-aot route-guide.main
  :resource-paths ["resources" "target/resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware     [lein-duct.plugin/middleware]
  :profiles
  {:repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :dev  [:shared :project/dev :profiles/dev]
   :test [:shared :project/dev :project/test :profiles/test]
   :uberjar [:shared :project/uberjar]

   :shared {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :dependencies   [[clj-http "3.12.3"]
                                   [com.bhauman/rebel-readline "0.1.4"]
                                   [eftest "0.6.0" :exclusions [org.clojure/tools.logging
                                                                org.clojure/tools.namespace]]
                                   [fipp "0.6.26"]
                                   [hawk "0.2.11"]
                                   [integrant/repl "0.3.3" :exclusions [integrant]]
                                   [orchestra "2021.01.01-1"]
                                   [pjstadig/humane-test-output "0.11.0"]]
                  :plugins [[jonase/eastwood "1.4.2"]
                            [lein-ancient "0.7.0"]
                            [lein-cloverage "1.2.4"]
                            [lein-codox "0.10.8"]]
                  :aliases {"rebel" ^{:doc "Run REPL with rebel-readline."}
                            ["trampoline" "run" "-m" "rebel-readline.main"]
                            "test-coverage" ^{:doc "Execute cloverage."}
                            ["cloverage" "--ns-exclude-regex" "^(:?dev|user)$" "--codecov" "--junit"]
                            "lint" ^{:doc "Execute eastwood."}
                            ["eastwood" "{:config-files [\"dev/resources/eastwood_config.clj\"]
                                          :source-paths [\"src\"]
                                          :test-paths []}"]}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]
                  :codox {:output-path "target/codox"
                          :source-uri "https://github.com/lagenorhynque/route-guide/blob/master/{filepath}#L{line}"
                          :metadata {:doc/format :markdown}}}
   :project/test {}
   :project/uberjar {:aot :all
                     :uberjar-name "route-guide.jar"}
   :profiles/dev {}
   :profiles/test {}})
