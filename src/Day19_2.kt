fun main() {
//    val input = readInput("Day19_test")
    val input = readInput("Day19")
    val patterns = input[0].split(", ").toSet()
    val designs = input.drop(2)

    val mem = mutableMapOf<String,Long>()
    fun possibles(design: String): Long {
        if (design.isEmpty()) return 1
        mem[design]?.let { return it }
        return (1..design.length)
            .filter { design.take(it) in patterns }
            .sumOf { possibles(design.drop(it)) }
            .also { mem[design] = it }
    }

    println( designs.sumOf{ possibles(it) } ) // 796449099271652
}
