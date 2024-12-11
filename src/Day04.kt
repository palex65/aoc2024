
typealias CharMap = Map2D<Char>

fun Lines.toCharMap(): CharMap = map { it.toList() }

fun main() {
    fun part1(m: CharMap): Int =
        m.allPositions().sumOf { pos ->
            allDirections.count { dir ->
                var p = pos
                "XMAS".all { m.getOrNull(p) == it .also { p += dir } }
            }
        }

    fun Position.isEdge(m: CharMap)= row == 0 || col == 0 || row == m.size-1 || col == m[0].size-1

    fun part2(m: CharMap): Int = m.allPositions().filter{ !it.isEdge(m) }
        .count { pos ->
            m[pos] == 'A' && run {
                val ul = m[pos + Direction.UP_LEFT]
                val dr = m[pos + Direction.DOWN_RIGHT]
                val ur = m[pos + Direction.UP_RIGHT]
                val dl = m[pos + Direction.DOWN_LEFT]
                (ul == 'M' && dr == 'S' || ul == 'S' && dr == 'M') &&
                (ur == 'M' && dl == 'S' || ur == 'S' && dl == 'M')
            }
        }

    val testInput = readInput("Day04_test").toCharMap()
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04").toCharMap()
    part1(input).println()  // 2514
    part2(input).println()  // 1888
}
