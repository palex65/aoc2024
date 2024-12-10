
typealias Map2D<T> = List<List<T>>

fun <T> Map2D<T>.allPositions(): Sequence<Position> = sequence {
    for (row in indices) for (col in get(row).indices) yield(Position(row, col))
}
operator fun <T> Map2D<T>.get(p: Position) = get(p.row)[p.col]
operator fun <T> Map2D<T>.contains(p: Position) = p.row in indices && p.col in get(p.row).indices
fun <T> Map2D<T>.getOrNull(p: Position) = if (p in this) get(p) else null

val orthogonalDirections = listOf(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)

typealias HeightMap = Map2D<Int>

fun main() {
    fun List<String>.toHeightMap(): HeightMap =
        map { line -> line.map { it - '0' } }

    fun HeightMap.nextPositionsOf(from: Position): List<Position> =
        orthogonalDirections
            .map { from + it }
            .filter { getOrNull(it) == get(from) + 1 }

    fun HeightMap.trailHeads(): List<Position> = allPositions()
        .filter { this[it] == 0 }.toList()

    fun HeightMap.trailEnds(from: Position): Set<Position> =
        if (get(from) == 9) setOf(from)
        else nextPositionsOf(from)
            .fold(emptySet()) { acc, it -> acc + trailEnds(it) }

    fun part1(hm: HeightMap): Int = hm.trailHeads()
        .sumOf { hm.trailEnds(from = it).size }

    fun HeightMap.trailPathsCount(from: Position): Int =
        if (get(from) == 9) 1
        else nextPositionsOf(from)
            .sumOf { trailPathsCount(it) }

    fun part2(hm: HeightMap): Int = hm.trailHeads()
        .sumOf { hm.trailPathsCount(from = it) }

    val testInput = readInput("Day10_test").toHeightMap()
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10").toHeightMap()
    part1(input).println() // 674
    part2(input).println() // 1372
}
