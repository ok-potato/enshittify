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

        article(classes = "upload-window") {
            header {
                h1 {
                    text("Post your own release")
                }
                div(classes = "upload-window-header-buttons") {
                    button(classes = "upload-window-close") {
                        div {
                            text("x")
                        }
                    }
                }
            }
            form(
                method = FormMethod.post,
                action = "upload",
                classes = "upload-form",
                encType = FormEncType.multipartFormData
            ) {
                fieldSet {
                    label {
                        htmlFor = "title"
                        text("Title")
                    }
                    input(type = InputType.text, name = "title") {
                        placeholder = "Title"
                        id = "title"
                    }

                    fieldSet(classes = "form-artists") {
                        label(classes = "artist-label") {
                            text("Artists")
                        }
                        button(type = ButtonType.button) {
                            id = "add-artist"
                            text("+")
                        }
                    }
                }

                fieldSet(classes = "form-cover-art") {
                    label {
                        text("Cover Art")
                    }
                    label {
                        id = "cover-art-select"
                        htmlFor = "cover-art"
                        img {
                            src = "/upload.svg"
                            id = "cover-art-preview"
                            width = "200"
                            height = "200"
                        }
                    }
                    input(type = InputType.file, name = "cover-art") {
                        accept = "image/png, image/jpeg"
                        id = "cover-art"
                        onChange = "displayPreview()"
                    }
                }


                fieldSet(classes = "form-tracks") {
                    label(classes = "track-label") {
                        text("Tracks")
                    }
                    button(type = ButtonType.button) {
                        id = "add-track"
                        text("+")
                    }
                }

                footer {
                    button(type = ButtonType.submit) {
                        id = "submit-upload-form"
                        text("Submit")
                    }
                }
            }
        }
    }
}