package com

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.handleStatuses() {
    install(StatusPages) {
        exception<Throwable> { call, cause -> this@handleStatuses.logAndRespond(call, cause) }

        status(HttpStatusCode.OK) { _, _ -> }

        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(notFoundPage, ContentType.Text.Html, status = status)
        }
    }
}

suspend fun Application.logAndRespond(call: ApplicationCall, cause: Throwable) {
    if (log.isDebugEnabled) {
        cause.printStackTrace()
    }

    when (cause) {
        is NotFoundException -> call.response.status(HttpStatusCode.NotFound)
        is BadRequestException -> call.response.status(HttpStatusCode.BadRequest)
        else -> {
            if (this.developmentMode) {
                call.respondText(cause.message ?: "Internal Error", status = HttpStatusCode.InternalServerError)

            } else {
                call.respondText("Internal Error", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}