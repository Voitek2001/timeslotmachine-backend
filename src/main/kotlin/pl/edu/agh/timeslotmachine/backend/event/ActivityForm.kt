package pl.edu.agh.timeslotmachine.backend.event

import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.LocalConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteEnum
import pl.edu.agh.timeslotmachine.backend.util.string.capitalize

// TODO: i18n

enum class ActivityForm(
    val title: String,
    val isObligatory: Boolean
) : RemoteConvertable<ActivityForm> {
    Lecture("Lecture", true),
    Classes("Classes", false);

    override fun toRemote() =
        name.lowercase()

    companion object : LocalConvertable<ActivityForm> {
        override fun toLocal(remote: RemoteEnum) =
            valueOf(remote.capitalize())
    }
}