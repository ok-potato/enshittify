# Project Description

This is a [Ktor](https://ktor.io/) web server project,
with a somewhat non-trivial backend built with Kotlin.

Some of the technologies used in this project are [Ktor](https://ktor.io/),
[Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html),
[Kotlin HTML DSL](https://kotlinlang.org/docs/typesafe-html-dsl.html),
[Moshi JSON library](https://github.com/square/moshi) and
[JAVE2](https://github.com/a-schild/jave2).

## # enshittify

Enshittify is a basic music streaming website.
The name is reference to its intentionally bad audio quality.

The main page features a list of all uploaded releases,
and an upload form to post your own.
You can specify title and artists names, cover art,
and a list of tracks with track title and audio file.
On upload, the new release appears on the main page,
and you are forwarded to the new release page.

_Note: The repository only features works under the CC-BY or CC-BY-NC license._

## # implementation

The pages, which feature a decent amount of dynamic content,
are  rendered server-side via the Kotlin HTML DSL.

Endpoints are routed in `Routing.kt`, static resources are served from files in `src/main/resources/static`,
and releases are served from `src/main/resources/releases`,
whereby each release gets a subdirectory named as a random UUID.

Release info is persisted as JSON files via Moshi.
The cover art is re-encoded via the Java standard library,
and the tracks are re-encoded using the JAVE2 FFMPEG wrapper.

Interactivity of the music player and upload form is achieved with some basic (statically served) CSS and JavaScript.


## # running the server

You should be able to run the server by simply importing the gradle project and executing the `run` command
(tested on Windows and Linux).

Otherwise, a fatJar of the most recent build is included in the repo,
so you should in any case be able to run the server with `docker-compose up -d app`

The server is exposed at `localhost:8080`

## # limitations

Since this server features only file writes, and no outgoing network IO or similar,
there are no non-blocking operations which could be taken advantage of by coroutines.

The project could persist its data, for instance in a relational database
(using something like [Exposed](https://github.com/JetBrains/Exposed) or
[jOOQ](https://github.com/jOOQ/jOOQ)), and/or serve content separately through a CDN of some kind,
which would presumably offer better fault tolerance and scalability.