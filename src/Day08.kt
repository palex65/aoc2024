
data class AntennasMap(val freqs: Map<Char,List<Position>>, val limits: Limits)

fun main() {
    operator fun Position.minus(other: Position) = Position(row - other.row, col - other.col)
    operator fun Position.plus(other: Position) = Position(row + other.row, col + other.col)

    fun List<String>.toAntennasMap(): AntennasMap {
        val limits = Limits(size, get(0).length)
        val map = buildMap<Char,List<Position>> {
            limits.forEachPosition { p ->
                val freq = this@toAntennasMap[p.row][p.col]
                if (freq != '.') this[freq] = getOrDefault(freq, emptyList()) + p
            }
        }
        return AntennasMap(map, limits)
    }

    fun AntennasMap.forEachPairInSameFrequency(block: (Position, Position) -> Unit) {
        freqs.values.forEach { locs ->
            locs.forEachIndexed { idx, p1 ->
                for(i in idx+1..<locs.size) block(p1,locs[i])
            }
        }
    }

    fun part1(am: AntennasMap): Int {
        val antinodes = mutableSetOf<Position>()
        am.forEachPairInSameFrequency { p1, p2 ->
            val dif = p2 - p1
            (p2+dif).let { if (it in am.limits) antinodes += it }
            (p1-dif).let { if (it in am.limits) antinodes += it }
        }
        return antinodes.size
    }

    fun part2(am: AntennasMap): Int {
        val antinodes = mutableSetOf<Position>()
        am.forEachPairInSameFrequency { p1, p2 ->
            val dif = p2 - p1
            var p = p2
            while (p in am.limits) { antinodes += p ; p += dif }
            p = p1
            while (p in am.limits) { antinodes += p ; p -= dif }
        }
        return antinodes.size
    }

    val testInput = readInput("Day08_test").toAntennasMap()
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08").toAntennasMap()
    part1(input).println() // 271
    part2(input).println() // 994
}
