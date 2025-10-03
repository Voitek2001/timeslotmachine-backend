package pl.edu.agh.timeslotmachine.backend.checker

class RequirementNotSatisfiedException(
    check: Check
) : Exception(
    "Requirement is not satisfied for check '${check::class.simpleName ?: "<unnamed-checker>"}'"
)