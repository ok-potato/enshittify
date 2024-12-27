package com.resources

import com.components.templates.navbar
import com.components.templates.styles
import com.constants.companyBrand
import com.constants.unknownArtist
import com.constants.unknownRelease
import com.models.ReleaseInfo
import kotlinx.html.*

fun HTML.feedPage(allReleaseInfo: Map<String, ReleaseInfo>) {
    head {
        title {
            text(companyBrand)
        }
        styles()
        script {
            src = "/feed.js"
            defer = true
        }
    }
    body {
        navbar()

        article {
            section(classes = "feed") {
                for ((releaseId, releaseInfo) in allReleaseInfo.entries) {
                    val releaseUrl = "release/$releaseId"
                    a(href = releaseUrl, classes = "feed-item") {
                        img(classes = "feed-cover") {
                            src = "$releaseUrl/cover.jpg"
                            alt = releaseInfo.title ?: unknownRelease
                            width = "400"
                            height = "400"
                        }
                        p(classes = "feed-title") {
                            text(releaseInfo.title ?: unknownRelease)
                        }
                        p(classes = "feed-artists") {
                            text(releaseInfo.artists.joinToString().takeIf { it.isNotBlank() } ?: unknownArtist)
                        }
                    }
                }
            }
        }

        a(href = "/upload") {
            id = "post-release"

            img {
                src = "/plus.svg"
                alt = "post your release"
            }
        }
    }
}