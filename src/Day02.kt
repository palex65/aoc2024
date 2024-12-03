import kotlin.math.abs

typealias Report = List<Int>
/*
    fun Report.isSafe(exceptIdx: Int = size): Boolean {
        fun get(idx: Int) = this[idx + if (idx < exceptIdx) 0 else 1]
        val size = size - if (exceptIdx < size) 1 else 0
        require(size >= 2) { "Report must have at least 2 elements" }
        val firstDiff = get(1) - get(0)
        if (firstDiff == 0 || firstDiff !in -3..3) return false
        val increase = firstDiff > 0
        for (idx in 2..<size) {
            val diff = get(idx) - get(idx-1)
            if (diff !in -3..3 || diff == 0 || increase != diff > 0) return false
        }
        return true
    }
*/

fun main() {
    fun List<String>.toReports(): List<Report> =
        map { line -> line.split(' ').map { it.toInt() } }

    fun Report.isSafe(): Boolean =
        zipWithNext{ a, b -> b - a }
            .run { (all{ it<0 } || all{ it>0 }) && all{ abs(it) in 1..3 } }

    fun Report.isSafeDampener(): Boolean =
        isSafe() || indices.any { idx ->
            (subList(0, idx) + subList(idx + 1, size)).isSafe()
        }

    fun part1(reports: List<Report>): Int =
        reports.count { it.isSafe() }

    fun part2(reports: List<Report>): Int =
        reports.count { it.isSafeDampener() }
        //reports.count { it.isSafe() || it.indices.any { idx -> it.isSafe(exceptIdx = idx) } }

    val testInput = readInput("Day02_test").toReports()
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02").toReports()
    part1(input).println()  // 663
    part2(input).println()  // 692
}
