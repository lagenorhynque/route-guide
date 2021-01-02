# route-guide

Route Guide, an example gRPC API (ported from https://github.com/grpc/grpc/tree/v1.34.0/examples/python/route_guide).

## Developing

### Prerequisites

- [Java (JDK)](http://openjdk.java.net/)
    - `java -version` >= 8 (1.8.0)
- [Leiningen](https://leiningen.org/)

### Setup

When you first clone this repository, run:

```sh
lein duct setup
```

This will create files for local configuration, and prep your system
for the project.

### Development environment

To begin developing, start with a REPL.

```sh
lein repl
```

With [rebel-readline](https://github.com/bhauman/rebel-readline):

```sh
$ lein rebel
```

Then load the development environment.

```clojure
user=> (dev)
:loaded
```

Run `go` to prep and initiate the system.

```clojure
dev=> (go)
:initiated
```

By default this creates a web server at <http://localhost:8080>.

When you make changes to your source files, use `reset` to reload any
modified files and reset the server.

```clojure
dev=> (reset)
:reloading (...)
:resumed
```

### Production build & run

```sh
$ lein uberjar
$ java -jar target/route-guide.jar
```

### Testing

Testing is fastest through the REPL, as you avoid environment startup
time.

```clojure
dev=> (test)
...
```

But you can also run tests through Leiningen.

```sh
lein test
```

with [cloverage](https://github.com/cloverage/cloverage):

```sh
$ lein test-coverage
# Open the coverage report
$ open target/coverage/index.html
```

### Linting

- [`eastwood`](https://github.com/jonase/eastwood)

```sh
$ lein lint
```

- [`cljstyle check`](https://github.com/greglook/cljstyle), [`clj-kondo`](https://github.com/borkdude/clj-kondo), [`joker`](https://github.com/candid82/joker)

```sh
$ make lint
```

- fixing formatting

```sh
$ make cljstyle-fix
```

### API Documentation ([Codox](https://github.com/weavejester/codox))

```sh
$ lein codox
$ open target/codox/index.html
```
