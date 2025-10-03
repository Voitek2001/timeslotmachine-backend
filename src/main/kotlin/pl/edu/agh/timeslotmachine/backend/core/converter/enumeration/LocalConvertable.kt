package pl.edu.agh.timeslotmachine.backend.core.converter.enumeration

import com.fasterxml.jackson.annotation.JsonCreator

interface LocalConvertable<L : Enum<L>> {
    @JsonCreator
    fun toLocal(remote: RemoteEnum): L
}