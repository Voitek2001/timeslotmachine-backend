package pl.edu.agh.timeslotmachine.backend.user

import pl.edu.agh.timeslotmachine.backend.core.EID

typealias IndexNo = Int

fun IndexNo.toEID(): EID = this.toLong()