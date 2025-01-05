package com.models

import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.Table


object ReleasesTable : Table("releases") {
    val id = uuid("id")
    val title = varchar("title", 500).nullable()

    override val primaryKey get() = PrimaryKey(id)

    init {
        index("idx_release_title", columns = arrayOf(title))
    }
}


object TracksTable : Table("tracks") {
    val releaseId = uuid("release_id")
    val number = integer("number")
    val title = varchar("title", 500).nullable()
    val lengthSeconds = integer("length_seconds")

    override val primaryKey get() = PrimaryKey(releaseId, number)

    init {
        foreignKey(releaseId to ReleasesTable.id, onDelete = CASCADE)

        index("idx_track_title", columns = arrayOf(title))
    }
}


object ArtistsTable : Table("artists") {
    val id = uuid("id")
    val name = varchar("name", 500)

    override val primaryKey get() = PrimaryKey(id)

    init {
        index("idx_artist_name", columns = arrayOf(name))
    }
}


object ReleaseFeaturesTable : Table("release_features") {
    val artistId = uuid("artist_id")
    val releaseId = uuid("release_id")

    override val primaryKey get() = PrimaryKey(artistId, releaseId)

    init {
        foreignKey(artistId to ArtistsTable.id)
        foreignKey(releaseId to ReleasesTable.id, onDelete = CASCADE)
    }
}


object TrackFeaturesTable : Table("track_features") {
    val artistId = uuid("artist_id")
    val releaseId = uuid("release_id")
    val number = integer("number")

    override val primaryKey get() = PrimaryKey(artistId, releaseId, number)

    init {
        foreignKey(artistId to ArtistsTable.id)
        foreignKey(releaseId to TracksTable.releaseId, number to TracksTable.number, onDelete = CASCADE)
    }
}