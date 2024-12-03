
fun main() {

    fun part1(mem: String): Int =
        Regex("""mul\((\d{1,3}),(\d{1,3})\)""").findAll(mem).sumOf { mr ->
            val (a, b) = mr.destructured.toList().map { it.toInt() }
            a * b
        }

    fun part2(mem: String): Int {
        val parts = mem.split("don't()")
        val enabledParts = parts[0] + parts.drop(1).joinToString("") {
            it.substringAfter("do()", missingDelimiterValue = "")
        }
        return part1(enabledParts)
    }

    val testInput = readInput("Day03_test").joinToString("")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    val input = readInput("Day03").joinToString("")
    part1(input).println()  // 189600467
    part2(input).println()  // 107069718
}
