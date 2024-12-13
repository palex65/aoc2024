typealias RegionsMap = Map2D<Char>
typealias Region = Set<Position>
typealias Regions = List<Region>

fun main() {
    fun Lines.toRegionsMap(): RegionsMap =
        map { it.toList() }

    fun RegionsMap.adjacentOf(p: Position): Region {
        val id = get(p)
        val region = mutableSetOf<Position>()
        val open = mutableListOf(p)
        while (open.isNotEmpty()) {
            val current = open.removeFirst()
            region.add(current)
            open.addAll( orthogonalDirections.map { current + it }
                .filter { getOrNull(it) == id && it !in region && it !in open }
            )
        }
        return region
    }

    fun RegionsMap.perimeterOf(r: Region): Int =
        r.sumOf { p -> orthogonalDirections.count { getOrNull(p + it) != get(p) } }

    fun RegionsMap.getAllRegions(): Regions = buildList {
        allPositions().forEach { p ->
            if (none { p in it }) add(adjacentOf(p))
        }
    }

    fun part1(map: RegionsMap): Int {
        val regions = map.getAllRegions()
        return regions.sumOf { r -> r.size * map.perimeterOf(r) }
    }

    data class Side(val p: Position, val dir: Direction){
        val isHorizontal get() = dir == Direction.UP || dir == Direction.DOWN

        fun isContiguousWith(s: Side): Boolean = dir == s.dir && when {
            isHorizontal ->  p.row==s.p.row && (p.col+1 == s.p.col || p.col-1 == s.p.col)
            else -> p.col==s.p.col && (p.row+1 == s.p.row || p.row-1 == s.p.row)
        }
    }

    fun RegionsMap.sidesOf(r: Region): Int {
        val unitSides = r.flatMap { p ->
            orthogonalDirections.filter { getOrNull(p + it) != get(p) }.map { Side(p,it) }
        }
        val sortedSides =
            unitSides.filter { it.isHorizontal }.sortedWith(compareBy({it.dir.dRow},{it.p.row},{it.p.col})) +
            unitSides.filter { !it.isHorizontal }.sortedWith(compareBy({it.dir.dCol},{it.p.col},{it.p.row}))
        val longSides = sortedSides.filterIndexed{ i, s -> i==0 || !s.isContiguousWith(sortedSides[i-1]) }
        return longSides.size
    }

    fun part2(map: RegionsMap): Int {
        val regions = map.getAllRegions()
        return regions.sumOf { r -> r.size * map.sidesOf(r) }
    }

    val testInput = readInput("Day12_test").toRegionsMap()
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)
    check(part1(readInput("Day12_test1").toRegionsMap()) == 140)
    check(part1(readInput("Day12_test2").toRegionsMap()) == 772)

    val input = readInput("Day12").toRegionsMap()
    part1(input).println() // 1464678
    part2(input).println() // 877492
}
