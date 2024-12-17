
fun main() {
    val input = readInput("Day16")
    val map: Puzzle = input.toCharMatrix()
    val end = map.find('E').also { map[it] = '.' }
    val start = map.find('S').also { map[it] = '.' }

    data class State(val pos: Position, val dir: Direction)
    data class ScoreState(val state: State, val score: Int)

    val open = mutableListOf(ScoreState(State(start, Direction.RIGHT),0))
    val closed = mutableMapOf<State,Int>()
    while (open.isNotEmpty()) {
        val curr = open.removeFirst()
        if ((closed[curr.state] ?: Int.MAX_VALUE) <= curr.score) continue
        closed[curr.state] = curr.score
        val next = orthogonalDirections
            .filter { it != curr.state.dir.opposite() && map[curr.state.pos+it] == '.' }
            .map {
                if (it == curr.state.dir) ScoreState(State(curr.state.pos+it, it), curr.score + 1)
                else ScoreState(State(curr.state.pos, it), curr.score + 1000)
            }
            .filter { it.score < (closed[it.state] ?: Int.MAX_VALUE) }
        open.addAll(next)
    }

    val path = mutableSetOf<Position>()
    var curr = listOf(closed.entries.filter { it.key.pos == end }.minBy { it.value }.key)
    while (curr.isNotEmpty()) {
        path.addAll(curr.map { it.pos })
        curr = curr.flatMap { k1 ->
            val p1 = k1.pos
            val s1 = checkNotNull(closed[k1])
            orthogonalDirections.mapNotNull { d ->
                val p2 = p1 + d
                if (p2 in path) return@mapNotNull null
                val k2 = State(p2,d.opposite())
                val s2 = closed[k2]
                k2.takeIf { s2!=null && (s2==s1-1 || s2==s1-1000 || s2==s1-1001) }
            }
        }.distinct()
    }
    println(path.size) // 545
}
