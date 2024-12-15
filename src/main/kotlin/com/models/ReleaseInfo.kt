package com.models

import kotlin.math.max

data class ReleaseInfo(
    val title: String? = null,
    val artists: List<String>,
    val tracks: List<Track>
): Json

data class Track(
    val title: String? = null,
    val artists: List<String>, // TODO this can currently not be specified in the upload form
    val length: Int
): Json

class ReleaseInfoBuilder {
    var title: String? = null
    val artists = mutableMapOf<Int, String>()
    val trackNames = mutableMapOf<Int, String>()
    val trackLengths = mutableMapOf<Int, Int>()

    operator fun invoke(): ReleaseInfo {
        val artistList = artists.entries.sortedBy { it.key }.map { it.value }

        val lastTrack = max(trackNames.keys.maxOrNull() ?: 0, trackLengths.keys.maxOrNull() ?: 0)
        val trackList = mutableListOf<Track>()
        for (trackNr in 1..lastTrack) {
            trackList.add(Track(trackNames[trackNr], listOf(), trackLengths[trackNr] ?: 0))
        }

        return ReleaseInfo(title, artistList, trackList.toList())
    }
}