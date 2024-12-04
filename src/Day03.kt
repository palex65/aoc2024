/*
const val PREFIX = "mul("
const val MIN_DIGS = 1
const val MAX_DIGS = 3

fun parseMult(s: String, from: Int): Int? {
    if (!s.startsWith(PREFIX,from)) return null
    val end1 = s.indexOf(',', from + PREFIX.length + MIN_DIGS)
    if (end1 == -1 || end1 - (from + PREFIX.length) > MAX_DIGS) return null
    val end2 = s.indexOf(')', end1 + 1 + MIN_DIGS)
    if (end2 == -1 || end2 - (end1 + 1) > MAX_DIGS) return null
    val a = s.substring(from + PREFIX.length, end1).toIntOrNull() ?: return null
    val b = s.substring(end1 + 1, end2).toIntOrNull() ?: return null
    return a * b
}

fun part1(mem: String): Int {
    var sum = 0
    var i = 0
    while (i < mem.length) {
        val m = parseMult(mem,i)
        i += if (m != null) { sum += m; 8 }
        else 1
    }
    return sum
}

fun part2(mem: String): Int {
    var sum = 0
    var i = 0
    var enable = true
    while (i < mem.length) {
        val m = parseMult(mem,i)
        i += when {
            m != null -> { if (enable) sum += m; 8 }
            mem.startsWith("don't()",i) -> { enable = false; 7 }
            mem.startsWith("do()",i) -> { enable = true; 4 }
            else -> 1
        }
    }
    return sum
}
*/

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
