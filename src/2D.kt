/**
 * 2D data structures and operations
 * Types: Position, Direction, Map2D, Limits
 */

data class Position(val row: Int, val col: Int) {
    override fun toString() = "($row, $col)"
}

data class Direction(val dRow: Int, val dCol: Int) {
    companion object {
        val UP = Direction(-1, 0)
        val DOWN = Direction(1, 0)
        val LEFT = Direction(0, -1)
        val RIGHT = Direction(0, 1)
        val UP_LEFT = Direction(-1, -1)
        val UP_RIGHT = Direction(-1, 1)
        val DOWN_LEFT = Direction(1, -1)
        val DOWN_RIGHT = Direction(1, 1)
    }
}

/**
 * All Directions in the order of rotate 45º
 */
val allDirections = listOf(
    Direction.UP, Direction.UP_RIGHT, Direction.RIGHT, Direction.DOWN_RIGHT,
    Direction.DOWN, Direction.DOWN_LEFT, Direction.LEFT, Direction.UP_LEFT
)
/**
 * Orthogonal Directions in the order of rotate 90º
 */
val orthogonalDirections = listOf(
    Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT
)

operator fun Position.plus(dir: Direction) = Position(row + dir.dRow, col + dir.dCol)

fun aroundOf(p: Position, dirs: List<Direction> =orthogonalDirections) = dirs.map { p + it }

/**
 * 2D map type
 */
typealias Map2D<T> = List<List<T>>

fun <T> Map2D<T>.allPositions(): Sequence<Position> = sequence {
    for (row in indices) for (col in get(row).indices) yield(Position(row, col))
}

operator fun <T> Map2D<T>.get(p: Position) = get(p.row)[p.col]

operator fun <T> Map2D<T>.contains(p: Position) = p.row in indices && p.col in get(p.row).indices

fun <T> Map2D<T>.getOrNull(p: Position) = if (p in this) get(p) else null

/**
 * Limits of a rectangular map
 */
data class Limits(val rows: Int, val cols:Int)

fun Position.inBounds(limits: Limits) = row in 0..<limits.rows && col in 0..<limits.cols

operator fun Limits.contains(p: Position) = p.inBounds(this)

fun Limits.allPositions(): Sequence<Position> = sequence {
    for (row in 0..<rows) for (col in 0..<cols) yield(Position(row, col))
}

inline fun Limits.forEachPosition(block: (Position) -> Unit) =
    allPositions().forEach(block)



typealias CharMatrix = List<CharArray>

fun Lines.toCharMatrix(): CharMatrix = map { it.toCharArray() }

operator fun CharMatrix.get(p: Position): Char = this[p.row][p.col]
operator fun CharMatrix.set(p: Position, c: Char) { this[p.row][p.col] = c }

fun CharMatrix.find(c: Char): Position {
    Limits(size, this[0].size).forEachPosition { p ->
        if (this[p] == c) return p
    }
    error("Not found $c")
}

fun CharMatrix.findAll(c: Char): List<Position> {
    val actors = mutableListOf<Position>()
    Limits(size, this[0].size).forEachPosition { p ->
        if (this[p] == c) actors.add(p)
    }
    return actors
}


typealias IntMatrix = List<IntArray>

fun intMatrix(rows: Int, cols: Int, init: (Int, Int) -> Int): IntMatrix =
    List(rows) { row -> IntArray(cols) { col -> init(row, col) } }





