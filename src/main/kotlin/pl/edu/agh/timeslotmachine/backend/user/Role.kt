package pl.edu.agh.timeslotmachine.backend.user

import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.LocalConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteEnum
import pl.edu.agh.timeslotmachine.backend.util.string.capitalize

// TODO: i18n

enum class Role(
    val title: String,
    val description: String
) : RemoteConvertable<Role> {
    Admin("Administrator", "Has access to everything"),
    User("User", "Can enroll in the specified groups");

    override fun toRemote(): RemoteEnum =
        name.lowercase()

    companion object : LocalConvertable<Role> {

        const val DEFAULT_ROLE_PREFIX = "ROLE_"

        override fun toLocal(remote: RemoteEnum) =
            valueOf(remote.capitalize())
    }

    fun toAuthorityString() = "$DEFAULT_ROLE_PREFIX${name.uppercase()}"
}
