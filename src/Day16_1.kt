typealias Puzzle = CharMatrix

fun Direction.opposite() =
    orthogonalDirections.indexOf(this).let { orthogonalDirections[(it+2) % 4] }

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
            .map { ScoreState(
                state = State(curr.state.pos+it, it),
                score = curr.score + if (it==curr.state.dir) 1 else 1001
            ) }
            .filter { it.score < (closed[it.state] ?: Int.MAX_VALUE) }
        open.addAll(next)
    }
    val scores = orthogonalDirections.mapNotNull { closed[State(end,it)] }
    println(scores.min()) // 104516
}
