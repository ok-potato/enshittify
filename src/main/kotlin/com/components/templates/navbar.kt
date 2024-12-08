package com.components.templates

import com.constants.companyBrand
import kotlinx.html.*

fun BODY.navbar() {
    nav {
        ul {
            li {
                a(href = "/") {
                    text(companyBrand)
                }
            }
        }
    }
}