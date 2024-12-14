data class Velocity(val dRow: Int, val dCol: Int)
data class Robot(val pos: Position, val vel: Velocity)

fun Position.move(v: Velocity, limits: Limits) = Position(
    (row + v.dRow).mod( limits.rows),
    (col + v.dCol).mod(limits.cols)
)

operator fun Velocity.times(n: Int) = Velocity(dRow*n, dCol*n)

fun main() {
    fun Lines.toRobots(): List<Robot> {
        fun String.toPair() = // p=0,4
            substringAfter('=').split(',').map { it.toInt() }
        fun String.toRobot() = // p=0,4 v=3,-3
            split(' ').let { (p,v) -> Robot(
                p.toPair().let{ (x,y) -> Position(y,x) },
                v.toPair().let{ (dx,dy) -> Velocity(dy, dx) }
            ) }
        return map { it.toRobot() }
    }

    fun List<Robot>.printMap(limits: Limits) {
        limits.allPositions().forEach { p ->
            val n = count { it.pos == p }
            print(if (n == 0) ' ' else '0' + n)
            if (p.col == limits.cols - 1) println(' ')
        }
        println(' ')
    }

    data class Quadrant(val from: Position, val to: Position)
    operator fun Quadrant.contains(p: Position) =
        p.row in from.row..<to.row && p.col in from.col..<to.col

    fun quadrantsOf(limits: Limits) = listOf(
        Quadrant(Position(0,0), Position(limits.rows/2, limits.cols/2)),
        Quadrant(Position(0,limits.cols/2+1), Position(limits.rows/2, limits.cols)),
        Quadrant(Position(limits.rows/2+1,0), Position(limits.rows, limits.cols/2)),
        Quadrant(Position(limits.rows/2+1,limits.cols/2+1), Position(limits.rows, limits.cols))
    )

    fun part1(r: List<Robot>, limits: Limits): Int {
        val positions = r.map { (p,v) -> p.move(v*100, limits) }
        val counts = quadrantsOf(limits).map { q -> positions.count { it in q } }
        return counts.reduce(Int::times)
    }

    fun part2(r: List<Robot>, limits: Limits): Int {
        val cr = limits.rows/2
        val cc = limits.cols/2
        val side = limits.rows/5
        val center = Quadrant(Position(cr-side,cc-side), Position(cr+side,cc+side))
        //println(center)
        var robots = r
        repeat (10000) {
            robots = robots.map { (p,v) -> Robot(p.move(v, limits),v) }
            val count = robots.count { it.pos in center }
            if (count > r.size/2) {
                //println("${it+1}: $count")
                //robots.printMap(limits)
                return it+1
            }
        }
        return 0
    }

    val testInput = readInput("Day14_test").toRobots()
    check(part1(testInput, Limits(7,11)) == 12)

    val input = readInput("Day14").toRobots()
    part1(input, Limits(103,101)).println() // 229868730
    part2(input, Limits(103,101)).println() // 7861
}
