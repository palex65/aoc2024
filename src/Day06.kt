data class Guard(val pos: Position, val dir: Direction)
data class Limits(val rows: Int, val cols:Int)
data class GuardMap(val guard: Guard, val obstructions: Set<Position>, val limits: Limits)

fun Position.inBounds(limits: Limits) =
    row in 0..<limits.rows && col in 0..<limits.cols

fun main() {
    fun List<String>.toGuardMap(): GuardMap {
        var guard: Guard? = null
        val obstructions = mutableSetOf<Position>()
        forEachIndexed{ row, line -> line.forEachIndexed{ col, cell ->
            val pos = Position(row,col)
            when(cell) {
                '#' -> obstructions.add(pos)
                '^' -> guard = Guard(pos, Direction.UP)
                'v' -> guard = Guard(pos, Direction.DOWN)
                '<' -> guard = Guard(pos, Direction.LEFT)
                '>' -> guard = Guard(pos, Direction.RIGHT)
            }
        }}
        return GuardMap(checkNotNull(guard){"No guard"},obstructions, Limits(size, get(0).length))
    }

    fun Direction.rotateRight() = when(this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        else -> error("Invalid direction")
    }

    fun Guard.move(obstructions: Set<Position>): Guard {
        var d = dir
        while(pos+d in obstructions)
            d = d.rotateRight()
        return Guard(pos + d, d)
    }

    fun visited(gm: GuardMap): Set<Position>? {
        val visited = mutableSetOf<Position>()
        val path = mutableSetOf(gm.guard)
        var guard = gm.guard
        while (guard.pos.inBounds(gm.limits)) {
            visited.add(guard.pos)
            path.add(guard)
            guard = guard.move(gm.obstructions)
            if (guard in path) return null // Loop
        }
        return visited
    }

    fun part1(gm: GuardMap): Int? = visited(gm)?.size

    fun part2(gm: GuardMap): Int {
        val options = checkNotNull(visited(gm)) { "Visited is a loop" } - gm.guard.pos
        return options.count {
            visited(gm.copy(obstructions = gm.obstructions+it)) == null
        }
    }

    val testGM = readInput("Day06_test").toGuardMap()
    check(part1(testGM) == 41)
    check(part2(testGM) == 6)

    val inputGM = readInput("Day06").toGuardMap()
    part1(inputGM).println()  // 4758
    part2(inputGM).println()  // 1670
}
