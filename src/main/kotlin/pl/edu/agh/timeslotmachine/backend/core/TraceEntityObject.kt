package pl.edu.agh.timeslotmachine.backend.core

import jakarta.persistence.Convert
import jakarta.persistence.Converter
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.EnumConverter
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.LocalConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteEnum
import pl.edu.agh.timeslotmachine.backend.util.string.capitalize
import java.sql.Timestamp



enum class TraceFlagStatus : RemoteConvertable<TraceFlagStatus> {
    Active,
    Deleted,
    Updated;

    override fun toRemote(): RemoteEnum =
        name.lowercase()

    companion object : LocalConvertable<TraceFlagStatus> {
        override fun toLocal(remote: RemoteEnum) =
            valueOf(remote.capitalize())
    }
}

@Converter
class TraceFlagStatusConverter : EnumConverter<TraceFlagStatus>(TraceFlagStatus::class)

abstract class TraceEntityObject : EntityObject() {

    var traceCreateTimestamp: Timestamp? = null

    var traceUpdateTimestamp: Timestamp? = null

    var traceDeleteTimestamp: Timestamp? = null

    @Convert(converter = TraceFlagStatusConverter::class)
    var traceFlag: TraceFlagStatus? = TraceFlagStatus.Active

    var traceVersion: Int? = 0
}
