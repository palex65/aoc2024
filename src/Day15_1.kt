fun main() {
    val input = readInput("Day15")
    val map: List<CharArray> = input
        .takeWhile { it.isNotEmpty() }
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

    fun List<CharArray>.findActors(c: Char): List<Position>  {
        val actors = mutableListOf<Position>()
        for (row in indices) for (col in this[row].indices)
                if (this[row][col] == c) actors.add(Position(row, col))
        return actors
    }

    operator fun List<CharArray>.get(p: Position): Char = this[p.row][p.col]
    operator fun List<CharArray>.set(p: Position, c: Char) { this[p.row][p.col] = c }

    var robot = map.findActors('@').single()
    val boxes = map.findActors('O').toMutableList()

    fun moveRobot(to: Position) {
        map[robot] = '.'
        robot = to
        map[robot] = '@'
    }

    fun pushBox(start: Position, move: Direction): Boolean {
        var next = start + move
        while (map[next] in "O") next += move
        if (map[next] == '.') {
            map[next] = 'O' // robot replace start box
            boxes.remove(start)
            boxes.add(next)
            return true
        }
        return false
    }

    moves.forEach { move ->
        val next = robot + move
        when (map[next]) {
            '.' -> moveRobot(next)
            in "O" -> if (pushBox(next, move)) moveRobot(next)
            else -> { } // Wall
        }
    }

    val result = boxes.sumOf { it.row * 100 + it.col }
    println(result) // 1476771
}
