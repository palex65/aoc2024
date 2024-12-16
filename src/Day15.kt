fun main() {
    data class Robot(val pos: Position, val moves: List<Direction>)
    data class Warehouse(val walls: List<Position>, val limits: Limits)
    data class Info(val robot: Robot, val boxes: List<Position>, val warehouse: Warehouse)

    fun Lines.toInfo(): Info {
        val (map, moveLines) = indexOf("").let { take(it) to drop(it+1) }
        val limits = Limits(map.size, map[0].length)
        val walls = mutableListOf<Position>()
        val boxes = mutableListOf<Position>()
        var robotPos: Position? = null
        limits.allPositions().forEach { p ->
            when(map[p.row][p.col]) {
                '#' -> walls.add(p)
                '@' -> robotPos = p
                'O' -> boxes.add(p)
            }
        }
        val moves = moveLines.flatMap { it.map { c -> when(c) {
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            '<' -> Direction.LEFT
            '>' -> Direction.RIGHT
            else -> error("Invalid move $c")
        } } }
        return Info(Robot(robotPos!!, moves), boxes, Warehouse(walls, limits))
    }

    fun part1(info: Info): Int {
        var r = info.robot.pos
        val b = info.boxes.toMutableList()
        val w = info.warehouse.walls
        for (dir in info.robot.moves) {
            val p = r + dir
            if (p in w) continue
            if (p in b) {
                var f = p
                val indices = buildList {
                    do add(b.indexOf(f)).also { f += dir }
                    while (f in b)
                }
                if (f in w) continue
                indices.forEach { b[it] = b[it] + dir }
            }
            r = p
        }
        return b.sumOf { it.row * 100 + it.col }
    }

    data class Box(val p1: Position, val p2: Position)
    operator fun Box.contains(p: Position) = p == p1 || p == p2
    operator fun List<Box>.contains(p: Position) = any { p in it }
    fun List<Box>.indexOf(p: Position) = indexOfFirst { p in it }
    fun Box.move(dir: Direction) = Box(p1 + dir, p2 + dir)

    fun part2(info: Info): Int {
        var r = info.robot.pos.copy(col = info.robot.pos.col * 2)
        val b = info.boxes.mapIndexed { id, p -> Box(Position(p.row,p.col*2), Position(p.row,p.col*2+1)) }.toMutableList()
        val w = info.warehouse.walls.flatMap { p -> listOf(Position(p.row,p.col*2),Position(p.row,p.col*2+1)) }
        fun pushHorizontal(p: Position, dir: Direction): List<Int>? {
            var f = p
            val indices = buildSet {
                do add(b.indexOf(f)).also { f += dir }
                while (f in b)
            }
            return indices.takeIf { f !in w }?.toList()
        }
        fun pushVertical(p: Position, dir: Direction): List<Int>? {
            var fs = listOf(p)
            val indices = mutableSetOf<Int>()
            do {
                val idxs = fs.map { b.indexOf(it) }.filter { it >= 0 }.distinct()
                indices.addAll(idxs)
                fs = fs.flatMap { f ->
                    idxs.flatMap { b[it].let { box ->
                        if (f in box) listOf(box.p1, box.p2) else emptyList()
                    } }
                }.map { it + dir }
            } while (fs.any { it in b })
            return indices.takeIf { !fs.any { it in w } }?.distinct()
        }

        for (dir in info.robot.moves) {
            val p = r + dir
            if (p in w) continue
            if (p in b) {
                val indices = when (dir.dCol) {
                    -1, +1 -> pushHorizontal(p, dir)
                    else -> pushVertical(p, dir)
                } ?: continue
                indices.forEach { b[it] = b[it].move(dir) }
            }
            r = p
        }
        return b.sumOf { it.p1.row * 100 + it.p1.col }
    }

    val test1Input = readInput("Day15_test1").toInfo()
    check(part1(test1Input) == 2028)
    val testInput = readInput("Day15_test").toInfo()
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    val test2Input = readInput("Day15_test2").toInfo()
    check(part2(test2Input) == 618)

    val input = readInput("Day15").toInfo()
    part1(input).println() // 1476771
    part2(input).println() // 1468005 (old 1500373)
}
