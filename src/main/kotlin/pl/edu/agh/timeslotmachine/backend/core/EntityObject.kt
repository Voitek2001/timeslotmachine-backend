package pl.edu.agh.timeslotmachine.backend.core

import pl.edu.agh.timeslotmachine.backend.util.string.stringify

abstract class GeneralEntityObject<ID> {
    abstract var id: ID

    override fun toString() =
        stringify(this)
}

abstract class EntityObject : GeneralEntityObject<EID?>()