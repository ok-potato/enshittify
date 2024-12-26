package com.resources

import com.components.templates.navbar
import com.components.templates.styles
import kotlinx.html.*

fun HTML.uploadForm() {
    head {
        title {
            text("Post Your Release")
        }
        styles("/styles.css", "upload-form.css")
        script {
            src = "/upload-form.js"
            defer = true
        }
    }
    body {
        navbar()
        article(classes = "upload-window") {
            header {
                h1 {
                    text("Post Your Release")
                }
            }
            form(
                method = FormMethod.post,
                action = "upload",
                classes = "upload-form",
                encType = FormEncType.multipartFormData
            ) {
                onSubmit = "formSubmitted(event)"

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
                    img {
                        src = "/spinner.svg"
                        id = "submit-form-spinner"
                    }
                }
            }
        }
    }
}