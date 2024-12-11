import kotlin.time.measureTime

typealias Stone = String
typealias Stones = List<Stone>

fun main() {
    fun Lines.toStones(): Stones =
        get(0).split(' ')

    fun Stone.blink(): Stones = when {
        this == "0"     -> listOf("1")
        length % 2 == 0 -> listOf(take(length/2), drop(length/2).toLong().toString())
        else            -> listOf((toLong() * 2024).toString())
    }

    val memStonesBlinks = mutableMapOf<Pair<Stone,Int>,Long>()

    fun Stone.numberOfStonesAfter(blinks: Int): Long {
        if (blinks == 0) return 1
        memStonesBlinks[this to blinks]?.let { return it }
        val stones = blink()
        return stones.sumOf { it.numberOfStonesAfter(blinks - 1) }
            .also { memStonesBlinks[this to blinks] = it }
    }

    fun part2(s: Stones, blinks: Int = 75): Long =
        s.sumOf { it.numberOfStonesAfter(blinks) }

    fun part1(stones: Stones, blinks: Int = 25): Int =
        if (blinks == 0) stones.size
        else part1(
            stones = stones.flatMap { it.blink() },
            blinks = blinks - 1
        )

    val testInput = readInput("Day11_test").toStones()
    measureTime { check(part1(testInput) == 55312) }.println()
    measureTime { check(part2(testInput, 25) == 55312L) }.println()

    val input = readInput("Day11").toStones()
    part1(input).println() // 224529
    //part1(input, 75).println()   // -> Out of memory
    part2(input).println() // 266820198587914
}
