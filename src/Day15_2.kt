fun main() {
    val input = readInput("Day15")
    val map: CharMatrix = input
        .takeWhile { it.isNotEmpty() }
        .map { it.flatMap { when(it) {
            '@' -> listOf('@','.')
            'O' -> listOf('[',']')
            else -> listOf(it, it)
        } } }
        .map { it.toCharArray() }

    val moves = input
        .dropWhile { it.isNotEmpty() }.drop(1)
        .flatMap { it.toList() }
        .map { when(it) {
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            '<' -> Direction.LEFT
            '>' -> Direction.RIGHT
            else -> error("Invalid move $it")
        } }

    var robot = map.find('@')
    val boxes = map.findAll('[').toMutableList()

    fun moveRobot(to: Position) {
        map[robot] = '.'
        robot = to
        map[robot] = '@'
    }

    fun pushBoxHorizontal(start: Position, move: Direction): List<Position>? {
        var next = start + move
        val pushed = mutableListOf<Position>()
        while (map[next] in "[]") {
            if (next in boxes) pushed.add(next)
            next += move
        }
        return pushed.takeIf { (map[next] == '.') }
    }

    fun pushBoxVertical(start: Position, move: Direction): List<Position>? {
        val next = mutableSetOf(start + move)
        val pushed = mutableSetOf<Position>()
        while (next.isNotEmpty()) {
            val n = next.first().also { next.remove(it) }
            when (map[n]) {
                '#' -> return null
                in "[]" -> {
                    next.add(n+move)
                    next.add(n+move+if (map[n]=='[') Direction.RIGHT else Direction.LEFT)
                    pushed.add(if (n in boxes) n else (n+Direction.LEFT))
                }
            }
        }
        return pushed.toList()
    }

    fun pushBox(start: Position, move: Direction): Boolean {
        val moved: List<Position> = when (move.dCol) {
            -1, +1 -> pushBoxHorizontal(start, move)
            else -> pushBoxVertical(start, move)
        } ?: return false
        moved.forEach {
            boxes.remove(it); boxes.add(it + move)
            map[it] = '.'; map[it+Direction.RIGHT] = '.'
        }
        moved.forEach { val p = it + move
            map[p] = '['; map[p+Direction.RIGHT] = ']'
        }
        return true
    }

    moves.forEach { move ->
        val next = robot + move
        when (map[next]) {
            '.' -> moveRobot(next)
            in "[]" -> if (pushBox(robot, move)) moveRobot(next)
            else -> { } // Wall
        }
    }

    val result = boxes.sumOf { it.row * 100 + it.col }
    println(result) // 1468005
}
