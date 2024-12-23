
fun main() {
//    val input = readInput("Day23_test")
    val input = readInput("Day23")
    val links = input.map { it.split("-") }
    val nodes = buildMap<String, MutableSet<String>> {
        fun add(a: String, b: String) = getOrPut(a) { mutableSetOf() }.add(b)
        links.forEach { (a, b) -> add(a, b); add(b, a) }
    }
    val mem = mutableSetOf<Pair<Set<String>,Set<String>>>()
    val sets = buildSet {
        fun find(closed: Set<String>, open:Set<String>) {
            val state = closed to open
            if (state in mem) return
            if (open.isEmpty()) add(closed)
            else open.forEach { n ->
                val lnks = nodes[n]!!
                if ( closed.any { it !in lnks } ) find(closed, open - n)
                else {
                    val cand = lnks.filter { it !in closed && it in open }
                    if (cand.isEmpty()) find(closed + n, open - n)
                    else cand.forEach { c -> find(closed + n, open - n + c) }
                }
            }
            mem.add(state)
            if (mem.size > 3000000) mem.clear()
        }
        nodes.entries.forEachIndexed { idx, (n1, links) ->
            //println("$idx of ${nodes.size}: $n1 ${mem.size}")
            find(setOf(n1), links.toSet())
        }
    }
    val set = sets.maxBy { it.size }
    println(set.sorted().joinToString(",")) // de,id,ke,ls,po,sn,tf,tl,tm,uj,un,xw,yz
}
