package com.resources

import com.components.templates.navbar
import com.components.templates.styles
import com.constants.unknownArtist
import com.constants.unknownRelease
import com.models.ReleaseInfo
import com.models.deserialize
import com.releasesBasePath
import io.ktor.server.plugins.*
import kotlinx.coroutines.*
import kotlinx.html.*
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

private const val coverUri = "cover.jpg"

// TODO do some caching here or something idk lol
suspend fun fetchReleaseInfo(releaseId: String): ReleaseInfo {
    val releaseInfo = try {
        withContext(Dispatchers.IO) {
            File("$releasesBasePath/$releaseId/info.json").readText()
        }.deserialize(ReleaseInfo::class)!!
    } catch (exception: Exception) {
        throw NotFoundException()
    }

    // TODO check that all files+info are there, so the client doesn't try fetching non-existent data
    return releaseInfo
}

suspend fun fetchAllReleaseInfo(): Map<String, ReleaseInfo> {
    return coroutineScope {
        Path(releasesBasePath).listDirectoryEntries("*").map { path ->
            val releaseId = path.name
            async {
                try {
                    releaseId to fetchReleaseInfo(releaseId)
                } catch (exception: Exception) {
                    null
                }
            }
        }.awaitAll().filterNotNull().toMap()
    }
}

fun fetchCover(releaseId: String): File {
    val file = File("$releasesBasePath/$releaseId/cover.jpg")
    if (file.exists()) return file else throw NotFoundException()
}

fun fetchTrack(releaseId: String, trackNr: Int): File {
    val file = File("$releasesBasePath/$releaseId/$trackNr.mp3")
    if (file.exists()) return file else throw NotFoundException()
}

fun HTML.releasePage(releaseId: String, releaseInfo: ReleaseInfo) {
    head {
        title {
            text(releaseInfo.title ?: unknownRelease)
        }
        styles()
        script {
            src = "/player.js"
            defer = true
        }
    }
    body {
        navbar()
        article {
            section(classes = "banner") {
                img {
                    alt = releaseInfo.title ?: unknownRelease
                    src = "$releaseId/$coverUri"
                    width = "400"
                    height = "400"
                }
                div(classes = "banner-details") {
                    p(classes = "banner-release") {
                        text(releaseInfo.title ?: unknownRelease)
                    }
                    p(classes = "banner-artists") {
                        text(releaseInfo.artists?.joinToString(", ") ?: unknownArtist)
                    }
                }
            }
            section(classes = "controls-bar") {
                button(classes = "play-button") {
                    img(src = "/play.svg", classes = "play-icon play-icon-play") {  }
                }
            }
            section(classes = "track-list") {
                ul {
                    releaseInfo.tracks.forEachIndexed() { idx, track ->
                        val trackNr = idx + 1

                        li(classes = "track") {
                            div(classes = "track-details") {
                                p(classes = "track-number-title") {
                                    text("$trackNr. ")
                                    span(classes = "track-title") {
                                        text(track.title ?: "Track $trackNr")
                                    }
                                }
                                p(classes = "track-artists") {
                                    text((track.artists ?: releaseInfo.artists)?.joinToString(", ") ?: unknownArtist)
                                }
                            }
                            div(classes = "track-length") {
                                text(track.length)
                            }
                        }
                    }
                }
            }
        }
    }
}