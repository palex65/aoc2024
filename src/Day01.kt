import kotlin.math.abs

fun main() {
    fun getLists(input: List<String>) = input.map {
        val (a, b) = it.trim().split("   ").map { it.toInt() }
        a to b
    }.unzip()

    fun part1(input: List<String>): Int {
        val (l1,l2) = getLists(input)
        val res = l1.sorted().zip(l2.sorted()).sumOf { abs(it.first - it.second) }
        return res
    }

    fun part2(input: List<String>): Int {
        val (l1,l2) = getLists(input)
        val res = l1.sumOf { n -> n * l2.count { it == n } }
        return res
    }

    // check(part1(listOf("test_input")) == 1)

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
