package pl.edu.agh.timeslotmachine.backend.exchange

import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.LocalConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteConvertable
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.RemoteEnum
import pl.edu.agh.timeslotmachine.backend.util.string.capitalize


enum class ExchangeStatus : RemoteConvertable<ExchangeStatus> {
    Pending,
    Done,
    Cancelled;

    override fun toRemote(): RemoteEnum =
        name.lowercase()

    companion object : LocalConvertable<ExchangeStatus> {
        override fun toLocal(remote: RemoteEnum) =
            valueOf(remote.capitalize())
    }
}