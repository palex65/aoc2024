import kotlin.system.measureTimeMillis

fun main() {
//    val input = readInput("Day19_test")
    val input = readInput("Day19")
    val patterns = input[0].split(", ").toSet()
    val designs = input.drop(2)

    fun isPossible(design: String): Boolean {
        if (design in patterns) return true
        for(n in design.length-1 downTo 1)
            if (design.take(n) in patterns && isPossible(design.drop(n)))
                return true
        return false
    }
    println( designs.count { isPossible(it) } ) // 267
}
