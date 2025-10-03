package pl.edu.agh.timeslotmachine.backend.util.algorithm

fun <T, K> Iterable<T>.multipleGroupBy(
    keySelector: (T) -> Iterable<K>
) = HashMap<K, HashSet<T>>().also { grouped ->
    forEach {
        keySelector(it).forEach { key ->
            grouped.computeIfAbsent(key) { HashSet() } += it
        }
    }
}