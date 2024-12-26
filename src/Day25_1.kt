typealias Schematic = List<String> // it.size==7 and it[0].length==5
typealias Heights = List<Int> // it.size==5 and it[i] in 0..5

fun main() {
//    val input = readInput("Day25_test")
    val input = readInput("Day25")
    val schematics = input.splitBy { it.isEmpty() }
    fun Schematic.toHeights(): Heights =
        get(0).indices.map { col -> indices.count{ row -> this[row][col]=='#'}-1 }
    val locks = schematics.filter { it[0] == "#####" }.map { it.toHeights() }
    val keys = schematics.filter { it[0] == "....." }.map { it.toHeights() }

    fun fit(key: Heights, lock: Heights) =
        key.indices.all { key[it] + lock[it] <= 5 }
    val count = locks.sumOf { lock -> keys.count { key -> fit(key,lock) } }
    println(count) // 2586
}
