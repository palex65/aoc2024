
fun bestPathLen(limits: Limits, fall: Set<Position>): Int? {
    val open = mutableSetOf(Position(0,0) to 0)
    val close = mutableMapOf(open.first())
    while (open.isNotEmpty()) {
        val (pos,len) = open.first().also { open.remove(it) }
        if (len <= (close[pos] ?: Int.MAX_VALUE)) close[pos] = len
        val l = len+1
        val next = orthogonalDirections.map { pos+it }
            .filter { p -> p in limits && p !in fall && l < (close[p] ?: Int.MAX_VALUE)}
        open.addAll( next.map { it to l })
    }
    return close[Position(limits.rows-1, limits.cols-1)]
}

fun main() {
//    val input = readInput("Day18_test")
//    val dim = 12
    val input = readInput("Day18")
    val dim = 1024
    val fall = input.map {
        val (x,y) = it.split(',').map { it.toInt() }
        Position(row = y, col = x)
    }
    val limits = Limits(
        rows = fall.maxOf { it.row }+1,
        cols = fall.maxOf { it.col }+1
    )
    println(bestPathLen(limits,fall.take(dim).toSet())) // 330
}
