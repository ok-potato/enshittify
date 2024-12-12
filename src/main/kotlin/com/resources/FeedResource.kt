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
                allReleaseInfo.entries.forEach { (releaseId, releaseInfo) ->
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
                            text(releaseInfo.artists?.joinToString() ?: unknownArtist)
                        }
                    }
                }
            }
        }

        form(action = "upload", classes = "upload-form") {
            label {
                htmlFor = "title"
                text("Title")
            }
            input(type = InputType.text, name = "title") {
                placeholder = "Title"
                id = "title"
            }

            div(classes = "form-artists") {
                label(classes = "artist-label") {
                    htmlFor = "artist-1"
                    text("Artists")
                }
                input(type = InputType.text, name = "artist") {
                    placeholder = "Artist 1"
                    id = "artist-1"
                }
            }

            button(type = ButtonType.button) {
                id = "add-artist"
                text("+")
            }

            label {
                htmlFor = "cover-art"
                text("Cover Art")
            }
            input(type = InputType.file, name = "cover-art") {
                accept = "image/png, image/jpeg"
                id = "cover-art"
                onChange = "displayPreview()"
            }
            img {
                id = "cover-art-preview"
                width = "200"
                height = "200"
            }

            div(classes = "form-tracks") {
                for (trackNr in 1..3) {
                    label(classes = "track-label") {
                        htmlFor = "track-$trackNr"
                        text("Tracks")
                    }
                    input(type = InputType.file, name = "track-file") {
                        id = "track-$trackNr-file"
                    }
                    input(type = InputType.text, name = "track-name") {
                        placeholder = "Track $trackNr"
                        id = "track-$trackNr-name"
                    }
                }
            }

            button(type = ButtonType.button) {
                id = "add-track"
                text("+")
            }

            button(type = ButtonType.submit) {
                id = "submit-upload-form"
                text("Submit")
            }
        }
    }
}