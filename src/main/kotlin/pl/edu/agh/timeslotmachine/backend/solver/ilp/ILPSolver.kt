package pl.edu.agh.timeslotmachine.backend.solver.ilp

import com.google.ortools.Loader
import com.google.ortools.linearsolver.MPObjective
import com.google.ortools.linearsolver.MPSolver
import pl.edu.agh.timeslotmachine.backend.preference.Preference


/**
 * ILPSolver class represents an Integer Linear Programming (ILP) model for scheduling based on user preferences.
 */

class ILPSolver {

    fun schedule(enrollmentList: List<Preference>): List<Preference> {
        Loader.loadNativeLibraries()

        val enrollmentWithoutImpossibility = handleImpossibility(enrollmentList)

        MPSolver.createSolver("BOP").run {
            val ilpEnrollments = enrollmentWithoutImpossibility.map { enrollment ->
                ILPEnrollment(
                    enrollment.user!!,
                    enrollment.concreteEvent!!,
                    enrollment.points,
                    makeBoolVar("enrollment_user")
                )
            }.asSequence()

            handleConflict(ilpEnrollments)
            handleUserLimits(ilpEnrollments)
            handleUserAssignmentLimits(ilpEnrollments)

            val objective = createObjective(ilpEnrollments)

            // Solve the ILP problem
            when (solve()) {
                MPSolver.ResultStatus.OPTIMAL -> {
                    println(objective.value())
                }
                else -> throw NoOptimalSolutionException()
            }

            return ilpEnrollments.map { ilpEnrollment ->
                Preference(ilpEnrollment.user, ilpEnrollment.concreteEvent, ilpEnrollment.points, ilpEnrollment.mpVariable.solutionValue() == 1.0)
            }.toList()

        }
    }

    /**
     * Remove preferences marked as impossible
     */

    private fun handleImpossibility(enrollmentList: List<Preference>): List<Preference> {
        return enrollmentList.filter { enrollment -> !enrollment.isImpossible }
    }

    /**
     * Constraint for overlapping enrollments of the same user
     * For each pair of variables if terms overlap, demand that
     * user is assigned only to one of them
     */

    private fun MPSolver.handleConflict(ilpEnrollments: Sequence<ILPEnrollment>) {

        ilpEnrollments.forEach { firstUserEnrollment ->
            ilpEnrollments.forEach { secondUserEnrollment ->
                if (firstUserEnrollment.concreteEvent.id != secondUserEnrollment.concreteEvent.id &&
                    firstUserEnrollment.concreteEvent.isConflict(secondUserEnrollment.concreteEvent) &&
                    firstUserEnrollment.user == secondUserEnrollment.user) {
                    makeConstraint(0.0, 1.0, "conflicts").run {
                        setCoefficient(firstUserEnrollment.mpVariable, 1.0)
                        setCoefficient(secondUserEnrollment.mpVariable, 1.0)
                    }
                }
            }
        }
    }

    /**
     * Constraint for limiting the number of user per term
     * For each term sum of all users is lower or equal to maxUsers per term
     *
     */
    private fun MPSolver.handleUserLimits(ilpEnrollments: Sequence<ILPEnrollment>) {
        ilpEnrollments.distinctBy { it.concreteEvent }.forEach { currEnrollment ->
            makeConstraint(0.0, currEnrollment.concreteEvent.userLimit.toDouble(), "userLimits").run {
                ilpEnrollments
                    .filter { it.concreteEvent == currEnrollment.concreteEvent }
                    .forEach { setCoefficient(it.mpVariable, 1.0) }
            }
        }
    }

    /**
     * Constraint for ensuring one enrollment per course per user.
     * So for each user and for each course which user is enrolled,
     * we create constraint that through all term of given course and given user
     * has value equal exactly 1.
     */

    private fun MPSolver.handleUserAssignmentLimits(ilpEnrollments: Sequence<ILPEnrollment>) {

        val allUsers = ilpEnrollments.map { it.user }.distinct()
        allUsers.forEach { user ->
            val userEvents = ilpEnrollments.filter { it.user == user }.map { it.concreteEvent.event }.distinct()
            userEvents.forEach { course ->
                course.pointLimits.keys.forEach { activityType ->
                    makeConstraint(1.0, 1.0, "assigment").run {
                        ilpEnrollments.forEach { ilpEnrollment ->
                            if (user.id == ilpEnrollment.user.id && ilpEnrollment.concreteEvent.event == course && ilpEnrollment.concreteEvent.activityForm == activityType) {
                                setCoefficient(ilpEnrollment.mpVariable, 1.0)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Objective function to maximize user interest ratings
     * For each user enrollment we would like to maximize sum of terms which given user received
     * multiply by userInterestRate for this term.
     */

    private fun MPSolver.createObjective(ilpEnrollments: Sequence<ILPEnrollment>): MPObjective {
        return objective().apply {
            ilpEnrollments.forEach {
                setCoefficient(it.mpVariable, it.points.toDouble())
            }
            setMaximization()
        }
    }


}