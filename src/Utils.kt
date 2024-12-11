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
