package pl.edu.agh.timeslotmachine.backend.util.string

fun String.capitalize() =
    replaceFirstChar { it.uppercaseChar() }