package com

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    if (developmentMode) log.info("DEV MODE")

    handleStatuses()
    configureDatabases()
    configureRouting()
}
