import kotlin.math.abs

typealias Report = List<Int>

fun main() {
    fun List<String>.toReports(): List<Report> =
        map { line -> line.split(' ').map { it.toInt() } }

    fun Report.isSafe(): Boolean =
        zipWithNext{ a, b -> b - a }
            .run { (all{ it<0 } || all{ it>0 }) && all{ abs(it) in 1..3 } }

    fun Report.isSafeDampener(): Boolean =
        isSafe() ||
        indices.any { idx -> (take(idx) + drop(idx + 1)).isSafe() }

    fun part1(reports: List<Report>): Int =
        reports.count { it.isSafe() }

    fun part2(reports: List<Report>): Int =
        reports.count { it.isSafeDampener() }

    val testInput = readInput("Day02_test").toReports()
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02").toReports()
    part1(input).println()  // 663
    part2(input).println()  // 692
}
