package com

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.handleStatuses() {
    install(StatusPages) {
        exception<NotFoundException> { call, _ ->
            call.response.status(HttpStatusCode.NotFound)
        }

        exception<Throwable> { call, cause ->
            call.respondText(cause.message ?: "Internal Error", status = HttpStatusCode.InternalServerError)
        }

        status(HttpStatusCode.OK) { _, _ -> }

        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(notFoundPage, ContentType.Text.Html, status = status)
        }
    }
}