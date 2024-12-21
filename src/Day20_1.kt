
fun main() {
    val input = readInput("Day20_test"); val min = 10
//    val input = readInput("Day20"); val min = 100
    val map: CharMatrix = input.map { it.toCharArray() }
    val start: Position = map.find('S')
    val end: Position = map.find('E')
    map[end] = '.'

    val path = buildMap {
        var prev: Position? = null
        var p = start
        var picosecs = 0
        while(p != end) {
            set(p, picosecs++)
            p = aroundOf(p).first { it != prev && map[it] == '.' }.also { prev = p }
        }
        set(p, picosecs)
    }
    val cheats = path.entries.sumOf { (p, time) ->
        aroundOf(p).filter { map[it] == '#' }.sumOf { p1 ->
            aroundOf(p1).count { p2 -> (path[p2] ?: 0) - time - 2 >= min }
        }
    }
    println(cheats) //1321
}
