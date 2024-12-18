
fun main() {
//    val input = readInput("Day18_test")
    val input = readInput("Day18")
    val fall = input.map {
        val (x,y) = it.split(',').map { it.toInt() }
        Position(row = y, col = x)
    }
    val limits = Limits(
        rows = fall.maxOf { it.row }+1,
        cols = fall.maxOf { it.col }+1
    )
    for (n in fall.indices)
        if( bestPathLen(limits,fall.dropLast(n).toSet()) != null ) {
            val (y,x) = fall[fall.size-n]
            println("$x,$y") // 10,38
            break
        }
}
