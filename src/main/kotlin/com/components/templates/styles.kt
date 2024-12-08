package com.components.templates

import kotlinx.html.HEAD
import kotlinx.html.link

fun HEAD.styles() {
    link(href = "/styles.css", rel = "stylesheet")
}