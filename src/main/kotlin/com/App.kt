package com

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    println("dev mode: $developmentMode")
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, statusCode ->
            call.respondText(notFoundPage, ContentType.Text.Html, status = statusCode)
        }
    }

    configureRouting()
}
