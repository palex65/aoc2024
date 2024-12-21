import kotlin.system.measureTimeMillis

fun main() {
//    val input = readInput("Day19_test")
    val input = readInput("Day19")
    val patterns = input[0].split(", ").toSet()
    val designs = input.drop(2)

    fun isPossible(design: String): Boolean = design in patterns ||
        (1 ..< design.length).any { n ->
            design.take(n) in patterns && isPossible(design.drop(n))
        }

    println( designs.count { isPossible(it) } ) // 267
}
