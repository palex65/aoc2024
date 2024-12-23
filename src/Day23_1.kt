
fun main() {
//    val input = readInput("Day23_test")
    val input = readInput("Day23")
    val links = input.map { it.split("-") }
    val nodes = buildMap<String, MutableSet<String>> {
        fun add(a: String, b: String) = getOrPut(a) { mutableSetOf() }.add(b)
        links.forEach { (a, b) -> add(a, b); add(b, a) }
    }
    val sets = buildSet {
        nodes.forEach { (n1, links) ->
            links.forEach { n2 -> nodes[n2]!!
                .filter { it != n1 && it in links }
                .forEach { n3 -> add(listOf(n1, n2, n3).sorted()) }
            }
        }
    }
    val tSets = sets.count { it.any { name -> name.first() == 't' } }
    println(tSets) // 1046
}
