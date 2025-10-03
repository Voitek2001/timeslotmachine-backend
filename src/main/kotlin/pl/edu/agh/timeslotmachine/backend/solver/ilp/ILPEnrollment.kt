package pl.edu.agh.timeslotmachine.backend.solver.ilp

import com.google.ortools.linearsolver.MPVariable
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.user.User

class ILPEnrollment(
    val user: User,
    val concreteEvent: ConcreteEvent,
    val points: Int,
    val mpVariable: MPVariable
) {
    override fun toString(): String {
        return "ILPEnrollment(user=$user, concreteEvent=$concreteEvent, points=$points, mpVariable=$mpVariable)"
    }
}