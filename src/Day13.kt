data class Button(val dx: Int, val dy: Int)
data class Location(val x: Int, val y: Int)
data class ClawMachine(val b1: Button, val b2: Button, val prize: Location)

fun main() {
    fun Lines.toMachines(): List<ClawMachine> {
        fun String.toButton(): Button {  // Button A: X+94, Y+34
            val (_,_,dx,dy) = split(" ")
            return Button(dx.substringAfter("X+").dropLast(1).toInt(), dy.substringAfter("Y+").toInt())
        }
        fun String.toLocation(): Location { // Prize: X=8400, Y=5400
            val (_,x,y) = split(" ")
            return Location(x.substringAfter("X=").dropLast(1).toInt(), y.substringAfter("Y=").toInt())
        }
        return windowed(4,4,true) { (b1, b2, prize) ->
            ClawMachine(b1.toButton(), b2.toButton(), prize.toLocation())
        }
    }

    /**
     * Solve a system of two linear equations in form: a1*x + b1*y = c1 ; a2*x + b2*y = c2
     * @return Pair(x,y) or null if no solution or not integer solution.
     */
    fun solve(a1: Int, b1: Int, c1: Long, a2: Int, b2: Int, c2: Long): Pair<Long, Long>? {
        val determinant = a1 * b2 - a2 * b1
        if (determinant == 0) return null
        val x = (c1 * b2 - c2 * b1).toDouble() / determinant
        val y = (a1 * c2 - a2 * c1).toDouble() / determinant
        val xl = x.toLong()
        val yl = y.toLong()
        if (xl.toDouble() != x || yl.toDouble() != y) return null
        return xl to yl
    }

    fun ClawMachine.tokensToPrize(add: Long = 0L): Long {
        val touches = solve(b1.dx, b2.dx, prize.x+add, b1.dy, b2.dy, prize.y+add) ?: return 0
        return touches.first * 3 + touches.second
    }

    fun part1(machines: List<ClawMachine>): Int =
        machines.sumOf { it.tokensToPrize() }.toInt()

    fun part2(machines: List<ClawMachine>): Long =
        machines.sumOf { it.tokensToPrize(add = 10000000000000L) }

    val testInput = readInput("Day13_test").toMachines()
    check(part1(testInput) == 480)
    check(part2(testInput) == 875318608908L)

    val input = readInput("Day13").toMachines()
    part1(input).println() // 28262
    part2(input).println() // 101406661266314
}
