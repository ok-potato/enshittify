package com.resources

import com.models.ReleaseInfo
import com.models.ReleaseInfoBuilder
import com.models.serialize
import com.releaseBaseUrl
import com.releasesBasePath
import com.util.saveJpeg
import com.util.saveMp3
import io.ktor.http.content.*
import io.ktor.util.logging.*
import java.io.File
import java.sql.DriverManager
import java.util.*
import org.jooq.impl.DSL
import generated.jooq.tables.references.*
import org.jooq.SQLDialect

val log = KtorSimpleLogger("Upload")

suspend fun MultiPartData.uploadRelease(): String {
    val uuid = releaseUUID()
    val path = "$releasesBasePath/$uuid"
    File(path).mkdir()
    val url = "$releaseBaseUrl/$uuid"

    val infoBuilder = ReleaseInfoBuilder()

    forEachPart { partData ->
        try {
            when (partData) {
                is PartData.FormItem -> partData.handleReleaseInfo(infoBuilder)
                is PartData.FileItem -> partData.handleReleaseFile(path, infoBuilder)
                else -> log.debug("Unhandled part data type {} on '{}'", partData::class, partData.name)
            }
        } catch (exception: Exception) {
            if (!File(path).deleteRecursively()) {
                println("Failed to delete release upon upload error: $uuid")
            }
            throw exception
        }
    }
    val releaseInfo = infoBuilder()

    val allArtists = (releaseInfo.tracks.flatMap { it.artists } + releaseInfo.artists).toSet()

    // TODO sql

    File("$path/info.json").writeText(infoBuilder().serialize(ReleaseInfo::class))

    return url
}

const val artistPrefix = "artist-"
const val trackNamePrefix = "track-name-"

fun PartData.FormItem.handleReleaseInfo(infoBuilder: ReleaseInfoBuilder) {
    val name = name ?: return
    if (value.isBlank()) return
    when {
        name == "title" -> infoBuilder.title = value

        name.startsWith(artistPrefix) -> {
            val artistNr = name.slice(artistPrefix.length..<name.length).toInt()
            infoBuilder.artists[artistNr] = value
        }

        name.startsWith(trackNamePrefix) -> {
            val trackNr = name.slice(trackNamePrefix.length..<name.length).toInt()
            infoBuilder.trackNames[trackNr] = value
        }

        else -> log.debug("Discarded unknown form item: {}", name)
    }
}

const val trackFilePrefix = "track-file-"

suspend fun PartData.FileItem.handleReleaseFile(path: String, infoBuilder: ReleaseInfoBuilder) {
    val name = name ?: return
    when {
        name == "cover-art" -> saveCoverArt("$path/cover.jpg")

        name.startsWith(trackFilePrefix) -> {
            val trackNr = name.slice(trackFilePrefix.length..<name.length).toInt()
            saveTrack(path, trackNr, infoBuilder)
        }

        else -> log.debug("Discarded unknown file item: {}", name)
    }
}

suspend fun PartData.FileItem.saveCoverArt(path: String) {
    saveJpeg(provider(), path)
}

suspend fun PartData.FileItem.saveTrack(path: String, trackNr: Int, infoBuilder: ReleaseInfoBuilder) {
    saveMp3(provider(), path, trackNr, infoBuilder)
}

fun releaseUUID(): UUID {
    for (attempt in 0..4) {
        val uuidCandidate = UUID.randomUUID()
        if (!File("$releasesBasePath/$uuidCandidate").exists()) {
            return uuidCandidate
        }
    }
    error("Couldn't find a unique ID for the release. This is likely due to a bug on the server.")
}

fun main() {
    DriverManager.getConnection("jdbc:postgresql://localhost:5432/enshittify", "postgres", "1234").use {
        val context = DSL.using(it, SQLDialect.POSTGRES)
        val result = context.select().from(ARTISTS).fetch()

        for (record in result) {
            println("${record.getValue(ARTISTS.ID)} ${record.getValue(ARTISTS.NAME)}")
        }
    }
}