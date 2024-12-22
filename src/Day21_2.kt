
fun main() {
    val input = readInput("Day21")
    val keypads = listOf(doorKeys) + List(25) { robotKeys }
    val mem = List(keypads.size) { mutableMapOf<String,Long>() }

    fun getLen(keypads: List<Keypad>, toPress: String): Long {
        val keypad = keypads.firstOrNull() ?: return toPress.length.toLong()
        mem[keypads.lastIndex][toPress]?.let { return it }
        var curr = keypad.start
        val len = toPress.sumOf { key ->
            val next = checkNotNull(keypad.map[key])
            paths(keypad, curr.also { curr=next }, next)
                .minOf { getLen(keypads.drop(1), it+"A") }
        }
        mem[keypads.lastIndex][toPress] = len
        return len
    }

    val lengths = input.map{ getLen(keypads,it) }
    val complexity = input.zip( lengths ).sumOf{ (code, length) ->
        code.dropLast(1).toInt() * length
    }
    println(complexity) // 230049027535970
}
