.PHONY: cljstyle-check
cljstyle-check:
	@cljstyle check

.PHONY: cljstyle-fix
cljstyle-fix:
	@cljstyle fix

.PHONY: clj-kondo-lint
clj-kondo-lint:
	@clj-kondo --lint src test

.PHONY: joker-lint
joker-lint:
	@joker --lint --working-dir .

.PHONY: lint
lint:
	@make cljstyle-check clj-kondo-lint joker-lint

.PHONY: gen-clj
gen-clj:
	protoc --clojure_out=grpc-server:src --proto_path=resources resources/route_guide/*.proto
