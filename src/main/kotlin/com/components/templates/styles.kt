package com.components.templates

import kotlinx.html.HEAD
import kotlinx.html.link

fun HEAD.styles(vararg sources: String = arrayOf("/styles.css")) {
    for (source in sources) {
        link(href = source, rel = "stylesheet")
    }
}