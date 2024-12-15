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
import io.ktor.utils.io.*
import java.io.ByteArrayInputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO

val log = KtorSimpleLogger("Upload")

suspend fun MultiPartData.uploadRelease(): String {
    val uuid = releaseUUID()
    val path = "$releasesBasePath/$uuid"
    File(path).mkdir()
    val url = "$releaseBaseUrl/$uuid"

    val infoBuilder = ReleaseInfoBuilder()

    forEachPart { partData ->
        when (partData) {
            is PartData.FormItem -> partData.handleReleaseInfo(infoBuilder)
            is PartData.FileItem -> partData.handleReleaseFile(path, infoBuilder)
            else -> log.debug("Unhandled part data type {} on '{}'", partData::class, partData.name)
        }
    }

    File("$path/info.json").writeText(infoBuilder().serialize(ReleaseInfo::class))

    return url
}

const val artistPrefix = "artist-"
const val trackNamePrefix = "track-name-"

fun PartData.FormItem.handleReleaseInfo(infoBuilder: ReleaseInfoBuilder) {
    val name = name ?: return
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
    }
}


const val trackFilePrefix = "track-file-"

// TODO this is almost definitely blocking
suspend fun PartData.FileItem.handleReleaseFile(path: String, infoBuilder: ReleaseInfoBuilder) {
    val name = name ?: return
    when {
        name == "cover-art" -> {
            saveCoverArt("$path/cover.jpg")
        }

        name.startsWith(trackFilePrefix) -> {
            val trackNr = name.slice(trackFilePrefix.length..<name.length).toInt()
            saveTrack(path, trackNr, infoBuilder)
        }
    }
}

suspend fun PartData.FileItem.saveCoverArt(path: String) {
    val bytes = this.provider().toByteArray()
    val image = ImageIO.read(ByteArrayInputStream(bytes))
    saveJpeg(image, path)
}

suspend fun PartData.FileItem.saveTrack(path: String, trackNr: Int, infoBuilder: ReleaseInfoBuilder) {
    saveMp3(this.provider(), path, trackNr, infoBuilder)
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