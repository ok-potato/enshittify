package com.models

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import java.util.*

object ReleaseTable : UUIDTable(name = "releases", columnName = "id") {
    val title = varchar("title", 500)
}

object TrackTable : Table()