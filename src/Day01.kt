import kotlin.math.abs

typealias Lists = Pair<List<Int>, List<Int>>

fun main() {
    fun Lines.toLists(): Lists = map {
        val (a, b) = it.trim().split("   ").map { it.toInt() }
        a to b
    }.unzip()

    fun part1(input: Lines): Int {
        val (l1,l2) = input.toLists()
        return l1.sorted().zip(l2.sorted()).sumOf { abs(it.first - it.second) }
    }

    fun part2(input: Lines): Int {
        val (l1,l2) = input.toLists()
        return l1.sumOf { n -> n * l2.count { it == n } }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println() // 1666427
    part2(input).println() // 24316233
}
