
typealias CharMatrix = List<String>

data class Position(val row: Int, val col: Int)

operator fun CharMatrix.get(p: Position) = get(p.row)[p.col]

fun Position.inBoundsOf(m: CharMatrix) = row in m.indices && col in m[0].indices

operator fun CharMatrix.contains(p: Position) = p.row in indices && p.col in get(0).indices

fun CharMatrix.allPositions(padding: Int = 0) =
    (padding..indices.last-padding).flatMap { row ->
        (padding..get(0).indices.last-padding).map { col -> Position(row, col) }
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
        val values = listOf(UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT)
    }
}

operator fun Position.plus(dir: Direction) = Position(row + dir.dRow, col + dir.dCol)

fun main() {
    fun part1(m: CharMatrix): Int =
        m.allPositions().sumOf { pos ->
            Direction.values.count { dir ->
                var p = pos
                "XMAS".all { p.inBoundsOf(m) && m[p] == it
                    .also { p += dir }
                }
            }
        }

    fun part2(m: CharMatrix): Int = m.allPositions(padding = 1)
        .count { pos ->
            m[pos] == 'A' && run {
                val ul = m[pos + Direction.UP_LEFT]
                val dr = m[pos + Direction.DOWN_RIGHT]
                val ur = m[pos + Direction.UP_RIGHT]
                val dl = m[pos + Direction.DOWN_LEFT]
                (ul == 'M' && dr == 'S' || ul == 'S' && dr == 'M') &&
                (ur == 'M' && dl == 'S' || ur == 'S' && dl == 'M')
            }
        }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()  // 2514
    part2(input).println()  // 1888
}
