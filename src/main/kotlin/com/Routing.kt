package com

import com.resources.feedPage
import com.resources.fetchReleaseInfo
import com.resources.releasePage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

const val resourcesBasePath = "src/main/resources"
val notFoundPage = File("$resourcesBasePath/404.html").readText()

fun Application.configureRouting() {
    routing {
        if (developmentMode) {
            get("{...}") {
                val path = call.request.path()
                try {
                    call.respondFile(File("$resourcesBasePath/static$path"))
                } catch (exception: Exception) {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }

        } else {
            staticResources("/", "static")
        }

        get("/") {
            call.respondHtml { feedPage() }
        }

        get("/release/{id}") {
            val releaseId = call.parameters["id"]!!
            val releaseInfo = fetchReleaseInfo(releaseId)

            if (releaseInfo != null) {
                call.respondHtml { releasePage(releaseId, releaseInfo) }
            } else {
                call.response.status(HttpStatusCode.NotFound)
            }
        }

    }
}
