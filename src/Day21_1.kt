import kotlin.math.absoluteValue

data class Keypad(val map: Map<Char,Position>) {
    val start = checkNotNull(map['A'])
    val space = checkNotNull(map[' '])
}

fun List<String>.toKeypad() = Keypad( buildMap {
    this@toKeypad.forEachIndexed { row, line -> line.forEachIndexed { col, c -> put(c, Position(row, col)) } }
} )
val doorKeys = listOf(
    "789",
    "456",
    "123",
    " 0A").toKeypad()
val robotKeys = listOf(
    " ^A",
    "<v>").toKeypad()

fun paths(keypad: Keypad, from: Position, to: Position): List<String> {
    val dRow = to.row - from.row
    val dCol = to.col - from.col
    val vKeys = (if (dRow > 0) "v" else "^").repeat(dRow.absoluteValue)
    val hKeys = (if (dCol > 0) ">" else "<").repeat(dCol.absoluteValue)
    return when {
        dRow == 0 -> listOf(hKeys)
        dCol == 0 -> listOf(vKeys)
        keypad.space.row == from.row && keypad.space.col == to.col -> listOf(vKeys + hKeys)
        keypad.space.row == to.row && keypad.space.col == from.col -> listOf(hKeys + vKeys)
        else -> listOf(vKeys + hKeys, hKeys + vKeys)
    }
}

fun main() {
//    val input = readInput("Day21_test")
    val input = readInput("Day21")

    fun getSequence(keypads: List<Keypad>, toPress: String): String {
        val keypad = keypads.firstOrNull() ?: return toPress
        var curr = keypad.start
        var keys = ""
        for(key in toPress) {
            val next = checkNotNull(keypad.map[key])
            val paths = paths(keypad, curr, next).map { it + "A" }
            keys += paths.map { getSequence(keypads.drop(1), it) }.minBy { it.length }
            curr = next
        }
        return keys
    }

    val sequences = input.map{ getSequence(listOf(doorKeys, robotKeys, robotKeys),it) }

    val complexity = input.zip( sequences ).sumOf{ (code, sequence) ->
        println("$code: $sequence")
        code.dropLast(1).toInt() * sequence.length
    }
    println(complexity) // 188398
}
