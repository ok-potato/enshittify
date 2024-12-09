package com

import com.models.ReleaseInfo
import com.resources.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

const val resourcesBasePath = "src/main/resources"
const val releasesBasePath = "$resourcesBasePath/releases"
val notFoundPage = File("$resourcesBasePath/404.html").readText()

fun Application.configureRouting() {
    routing {
        if (developmentMode) {
            get("/{...}") {
                val path = call.request.path()
                try {
                    call.respondFile(File("$resourcesBasePath/static/$path"))
                } catch (exception: Exception) { throw NotFoundException() }
            }

        } else {
            staticResources("/", "static")
        }

        get("/") {
            val allReleaseInfo: Map<String, ReleaseInfo> = fetchAllReleaseInfo()
            call.respondHtml { feedPage(allReleaseInfo) }
        }

        get("/release/{id}") {
            val releaseId = call.parameters["id"]!!
            val releaseInfo = fetchReleaseInfo(releaseId)
            call.respondHtml { releasePage(releaseId, releaseInfo) }
        }

        get("/release/{id}/cover.jpg") {
            val releaseId = call.parameters["id"]!!
            call.respondFile(fetchCover(releaseId))
        }

        get("/release/{id}/{trackNr}") {
            val releaseId = call.parameters["id"]!!
            val trackNr = call.parameters["trackNr"]!!.toInt()
            call.respondFile(fetchTrack(releaseId, trackNr))
        }

    }
}
