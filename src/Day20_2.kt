import kotlin.math.abs

fun main() {
//    val input = readInput("Day20_test"); val min = 50
    val input = readInput("Day20"); val min = 100
    val map: CharMatrix = input.map { it.toCharArray() }
    val end: Position = map.find('E').also { map[it] = '.' }
    val limits = Limits(map.size, map[0].size)

    val path = buildMap {
        var prev: Position? = null
        var p = map.find('S')
        var picosecs = 0
        while(p != end) {
            set(p, picosecs++)
            p = aroundOf(p).first { it != prev && map[it] == '.' }.also { prev = p }
        }
        set(p, picosecs)
    }

    val maxDistance = checkNotNull(path[end])
    val range = -20..20
    val deltas = range.flatMap { dr -> range.map { dr to it } }
        .filter { (dr,dc) -> abs(dr) + abs(dc) <= 20 }
        .map { (dr,dc) -> Triple(dr, dc, abs(dr) + abs(dc)) }
    val cheats = limits.allPositions()
        .mapNotNull { p -> path[p]?.let { p to it } }
        .sumOf { (p1, d1) -> deltas
            .mapNotNull { (dr, dc, abs) -> Position(p1.row+dr, p1.col+dc).takeIf { it in limits }?.let { it to abs } }
            .count { (p2, abs) ->
                path[p2]?.let { d2 -> d1 + maxDistance - d2 + abs <= maxDistance - min } ?: false
            }
        }
    println(cheats) // 971737    println(cheats) // 971737
}
