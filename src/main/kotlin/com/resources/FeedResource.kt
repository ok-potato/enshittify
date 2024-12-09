package com.resources

import com.components.templates.navbar
import com.components.templates.styles
import com.constants.companyBrand
import com.constants.unknownRelease
import com.models.ReleaseInfo
import kotlinx.html.*

fun HTML.feedPage(allReleaseInfo: Map<String, ReleaseInfo>) {
    head {
        title {
            text(companyBrand)
        }
        styles()
    }
    body {
        navbar()

        article {
            section(classes = "feed") {
                allReleaseInfo.entries.forEach { (releaseId, releaseInfo) ->
                    val releaseUrl = "release/$releaseId"
                    a(href = releaseUrl) {
                        img {
                            src = "$releaseUrl/cover.jpg"
                            alt = releaseInfo.title ?: unknownRelease
                            width = "400"
                            height = "400"
                        }
                    }
                }
            }
        }
    }
}