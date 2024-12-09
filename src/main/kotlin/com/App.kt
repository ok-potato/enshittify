package com

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    println("dev mode: $developmentMode")

    handleStatuses()
    configureRouting()
}
