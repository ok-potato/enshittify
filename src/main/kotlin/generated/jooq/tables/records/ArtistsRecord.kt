/*
 * This file is generated by jOOQ.
 */
package generated.jooq.tables.records


import generated.jooq.tables.Artists

import java.util.UUID

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class ArtistsRecord() : UpdatableRecordImpl<ArtistsRecord>(Artists.ARTISTS) {

    open var id: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    open var name: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    /**
     * Create a detached, initialised ArtistsRecord
     */
    constructor(id: UUID? = null, name: String? = null): this() {
        this.id = id
        this.name = name
        resetChangedOnNotNull()
    }
}
