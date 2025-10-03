package pl.edu.agh.timeslotmachine.backend.solver

import com.google.ortools.Loader
import com.google.ortools.linearsolver.MPSolver
import com.google.ortools.linearsolver.MPSolver.ResultStatus

// Source:
// https://developers.google.com/optimization/lp/lp_example

fun main() {
    Loader.loadNativeLibraries()

    MPSolver.createSolver("GLOP").run {
        val infinity = Double.POSITIVE_INFINITY

        // x and y are continuous non-negative variables.
        val x = makeNumVar(0.0, infinity, "x")
        val y = makeNumVar(0.0, infinity, "y")

        println("Number of variables = ${numVariables()}")

        // x + 2*y <= 14.
        makeConstraint(-infinity, 14.0, "c0").run {
            setCoefficient(x, 1.0)
            setCoefficient(y, 2.0)
        }

        // 3*x - y >= 0.
        makeConstraint(0.0, infinity, "c1").run {
            setCoefficient(x, 3.0)
            setCoefficient(y, -1.0)
        }

        // x - y <= 2.
        makeConstraint(-infinity, 2.0, "c2").run {
            setCoefficient(x, 1.0)
            setCoefficient(y, -1.0)
        }

        println("Number of constraints = ${numConstraints()}")

        // Maximize 3 * x + 4 * y.
        val objective = objective().apply {
            setCoefficient(x, 3.0)
            setCoefficient(y, 4.0)
            setMaximization()
        }

        when (solve()) {
            ResultStatus.OPTIMAL -> {
                println("Solution:")
                println("Objective value = ${objective.value()}")
                println("x = ${x.solutionValue()}")
                println("y = ${y.solutionValue()}")
            }
            else -> println("The problem does not have an optimal solution!")
        }

        println("\nAdvanced usage:")
        println("Problem solved in ${wallTime()} milliseconds")
        println("Problem solved in ${iterations()} iterations")
    }
}