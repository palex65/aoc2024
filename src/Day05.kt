typealias Rules = Map<Int, Set<Int>>
typealias Update = List<Int>

fun main() {
    fun List<String>.toRules(): Rules =
        takeWhile { it.isNotEmpty() }
            .map { it.split('|').map(String::toInt) }
            .groupBy({ it[0] }, { it[1] })
            .mapValues { it.value.toSet() }

    fun List<String>.toUpdates(): List<Update> =
        drop( indexOf("")+1 )
            .map { it.split(',').map(String::toInt) }

    fun Update.isValidFor(rules: Rules): Boolean =
        size==1 || drop(1).let {
            rules[first()]?.containsAll(it) == true && it.isValidFor(rules)
        }

    fun List<Update>.sumOfMidPages() = sumOf { it[ it.size/2 ] }

    fun part1(rules: Rules, updates: List<Update>): Int = updates
        .filter { it.isValidFor(rules) }
        .sumOfMidPages()

    fun Update.sortBy(rules: Rules): Update =
        sortedWith { p1, p2 -> if (rules[p1]?.contains(p2) == true) -1 else 1 }

    fun part2(rules: Rules, updates: List<Update>): Int = updates
        .filterNot { it.isValidFor(rules) }
        .map { it.sortBy(rules) }
        .sumOfMidPages()

    val testInput = readInput("Day05_test")
    val testRules = testInput.toRules()
    val testUpdates = testInput.toUpdates()
    check(part1(testRules,testUpdates) == 143)
    check(part2(testRules,testUpdates) == 123)

    val input = readInput("Day05")
    val rules = input.toRules()
    val updates = input.toUpdates()
    part1(rules,updates).println()  // 6041
    part2(rules,updates).println()  // 4884
}
