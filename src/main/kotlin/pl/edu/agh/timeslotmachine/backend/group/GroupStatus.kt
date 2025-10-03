package pl.edu.agh.timeslotmachine.backend.group

import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.LocalConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteEnum
import pl.edu.agh.timeslotmachine.backend.util.string.capitalize

enum class GroupStatus : RemoteConvertable<GroupStatus> {
    Closed,
    Open,
    Busy,
    Ready,
    Exchange,
    Available;

    override fun toRemote(): RemoteEnum =
        name.lowercase()

    companion object : LocalConvertable<GroupStatus> {
        override fun toLocal(remote: RemoteEnum) =
            valueOf(remote.capitalize())
    }
}