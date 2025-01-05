package com

import com.models.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    Database.connect("jdbc:postgresql://localhost:5432/enshittify", user = "postgres", password = "1234")

    transaction {
        SchemaUtils.create(ReleasesTable, TracksTable, ArtistsTable, ReleaseFeaturesTable, TrackFeaturesTable)
    }
}