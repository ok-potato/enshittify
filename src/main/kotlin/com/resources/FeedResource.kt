package com.resources

import com.components.templates.navbar
import com.components.templates.styles
import com.constants.companyBrand
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.title

fun HTML.feedPage() {
    head {
        title {
            text(companyBrand)
        }
        styles()
    }
    body {
        navbar()
    }
}