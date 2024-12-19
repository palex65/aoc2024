fun main() {
//    val input = readInput("Day19_test")
    val input = readInput("Day19")
    val patterns = input[0].split(", ").toSet()
    val designs = input.drop(2)

    val mem = mutableMapOf<String,Long>()

    fun possibles(design: String): Long {
        mem[design]?.let { return it }
        var total = if (design in patterns) 1L else 0
        for (n in 1 ..< design.length)
            if (design.take(n) in patterns)
                total += possibles(design.drop(n))
        mem[design] = total
        return total
    }
    println( designs.sumOf{ possibles(it) } ) // 796449099271652
}
