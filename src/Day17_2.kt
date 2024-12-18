fun main() {
    val input = readInput("Day17")
    val prog = input.toProg()

    val valuesOfA = mutableSetOf(0L)
    for(i in prog.indices) {
        val output = prog[prog.size-1-i]
        valuesOfA.toList().forEach { a ->
            valuesOfA.remove(a)
            (0..7).forEach { d ->
                val rA = a*8+d
                val m = CPU(rA = rA)
                m.exec(prog)
                if (m.output[0] == output)
                    valuesOfA.add(rA)
            }
        }
    }
    println(valuesOfA.min()) // 216549846240877
}
