package pl.edu.agh.timeslotmachine.backend.core.converter.enumeration

import com.fasterxml.jackson.annotation.JsonValue

interface RemoteConvertable<L : Enum<L>> {
    fun toRemote(): RemoteEnum

    @JsonValue
    fun toJsonValue(): String =
        toRemote()
}