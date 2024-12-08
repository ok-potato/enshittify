package com.models

data class ReleaseInfo(
    val title: String? = null,
    val artists: List<String>? = null,
    val tracks: List<Track>
): Json

data class Track(
    val title: String? = null,
    val artists: List<String>? = null,
    val length: String
): Json