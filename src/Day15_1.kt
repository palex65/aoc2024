fun main() {
    val input = readInput("Day15")
    val map: CharMatrix = input
        .takeWhile { it.isNotEmpty() }
        .toCharMatrix()

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
    val boxes = map.findAll('O').toMutableList()

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
