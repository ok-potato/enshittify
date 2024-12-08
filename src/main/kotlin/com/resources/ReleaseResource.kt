package com.resources

import com.components.templates.navbar
import com.components.templates.styles
import com.models.ReleaseInfo
import com.models.deserialize
import com.resourcesBasePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.*
import java.io.File


private const val releaseInfoBasePath = "$resourcesBasePath/release-info"

suspend fun fetchReleaseInfo(releaseId: String): ReleaseInfo? {
    val releaseInfo = try {
        withContext(Dispatchers.IO) {
            File("$releaseInfoBasePath/$releaseId.json").readText()
        }.deserialize(ReleaseInfo::class)
    } catch (exception: Exception) {
        null
    }

    // TODO check that all files+info are there, so the client doesn't try fetching non-existent data

    return releaseInfo
}

private const val coverUri = "cover.jpg"

private const val unknownRelease = "Unknown Release"
private const val unknownArtist = "Unknown Artist"

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

            }
            section(classes = "track-list") {
                ul {
                    releaseInfo.tracks.forEachIndexed() { idx, track ->
                        li(classes = "track") {
                            val trackNr = idx + 1
                            div(classes = "track-details") {
                                p(classes = "track-title") {
                                    text("$trackNr. ")
                                    text(track.title ?: "Track $trackNr")
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