import kotlin.io.path.Path
import kotlin.io.path.readText

typealias Lines = List<String>

/**
 * Reads all lines from the given input txt file.
 */
fun readInput(name: String): Lines =
    Path("src/input/$name.txt").readText().trim().lines()

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Splits a list into multiple lists separated by the elements that match the predicate.
 */
fun <T> List<T>.splitBy( predicate: (T)->Boolean ): List<List<T>> = buildList {
    val input = this@splitBy
    var from = 0
    var to = 0
    while (to < input.size) {
        while(to < input.size && !predicate(input[to])) ++to
        add(input.subList(from, to))
        from = ++to
    }
}
