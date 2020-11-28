# MovAPI

Simple Web Application using [Vert.X](https://vertx.io) which consumes data from [TMDB](https://www.themoviedb.org) allowing users to search for movies and respective cast and actors.

This project was developed as a three-phased project in Modelação e Padrões de Desenho (Modeling and Design Patterns) subject in [ISEL](https://www.isel.pt) during my degree in Computer Science and Computer Engineering.

There was three phases in the project:

* Phase 1 => The objective was to implement Java `Stream` methods using simple `Iterator`'s and developing the needed methods for retrieving information from TMDB using those methods
* Phase 2 => The objective was to replace all usage of our `Queries` class by Java `Stream`'s and make the needed changes
* Phase 3 => The objective was to use non-blocking IO. For that we used an Asynchronous client ([AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client)) and made all WebApi methods return a Java `CompletableFuture` which represents a future.

## Learning objectives

* Design Patterns
* Functional Programming in Java 8
* Lambdas
* Java [`Iterable`](https://docs.oracle.com/javase/8/docs/api/java/lang/Iterable.html)
* Java [`Stream`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)
* Java [`Spliterator`](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)
* Asynchronous Programming in Java
* Java [`CompletableFuture`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
* Unit testing
* Test Driven Development (TDD)

## Technologies used

* Java 8
* [Vert.X](https://vertx.io)

## Dependencies

* [Guava](https://github.com/google/guava)
* [GSON](https://github.com/google/gson)
* [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client)
* [Vert.X](https://vertx.io)

## Authors

This project was developed with [Ana Gaspar](https://github.com/ximenes13).
