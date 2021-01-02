(defproject route-guide "0.1.0"
  :description "Route Guide, an example gRPC API"
  :url "https://github.com/lagenorhynque/route-guide"
  :min-lein-version "2.8.1"
  :dependencies [[com.google.protobuf/protobuf-java "3.14.0"]
                 [duct.module.cambium "1.2.0"]
                 [duct.module.pedestal "2.1.2" :exclusions [io.pedestal/pedestal.jetty]]
                 [duct/core "0.8.0"]
                 [integrant "0.8.0"]
                 [io.undertow/undertow-core "2.2.3.Final"]
                 [io.undertow/undertow-servlet "2.2.3.Final"]
                 [org.clojure/clojure "1.10.1"]
                 [protojure "1.5.12"]
                 [protojure/google.protobuf "0.9.1"]]
  :plugins [[duct/lein-duct "0.12.1"]]
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
                  :dependencies   [[clj-http "3.11.0"]
                                   [com.bhauman/rebel-readline "0.1.4"]
                                   [eftest "0.5.9" :exclusions [org.clojure/tools.namespace]]
                                   [fipp "0.6.23"]
                                   [hawk "0.2.11"]
                                   [integrant/repl "0.3.2"]
                                   [orchestra "2021.01.01-1"]
                                   [pjstadig/humane-test-output "0.10.0"]]
                  :plugins [[jonase/eastwood "0.3.12"]
                            [lein-ancient "0.6.15"]
                            [lein-cloverage "1.2.1"]
                            [lein-codox "0.10.7"]]
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
                  :cljfmt {:indents {fdef [[:inner 0]]
                                     for-all [[:inner 0]]
                                     when-valid [[:inner 0]]}}
                  :codox {:output-path "target/codox"
                          :source-uri "https://github.com/lagenorhynque/route-guide/blob/master/{filepath}#L{line}"
                          :metadata {:doc/format :markdown}}}
   :project/test {}
   :project/uberjar {:aot :all
                     :uberjar-name "route-guide.jar"}
   :profiles/dev {}
   :profiles/test {}})
