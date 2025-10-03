package pl.edu.agh.timeslotmachine.backend.util.algorithm

fun <T> Set<T>.subtractFrom(from: Iterable<T>) =
    from - this