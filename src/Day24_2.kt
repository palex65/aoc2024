
fun adderGates(numBits: Int) = buildList {
    val halfAdder = listOf(
        Gate(GateType.XOR, "x00", "y00", "z00"),
        Gate(GateType.AND, "x00", "y00", "c00")
    )
    fun fullAdder(n: Int, last: Boolean): List<Gate> {
        val nn = "%02d".format(n)
        val pp = "%02d".format(n-1)
        val out = if (last) "z%02d".format(n + 1) else "c$nn"
        return listOf(
            Gate(GateType.XOR, "x$nn", "y$nn", "s$nn"),
            Gate(GateType.AND, "x$nn", "y$nn", "d$nn"),
            Gate(GateType.XOR, "c$pp", "s$nn", "z$nn"),
            Gate(GateType.AND, "c$pp", "s$nn", "e$nn"),
            Gate(GateType.OR, "e$nn", "d$nn", out)
        )
    }
    addAll(halfAdder)
    for (n in 1..<numBits) addAll(fullAdder(n, last = n == numBits-1))
}

fun main() {
    val input = readInput("Day24")
    val (w,g) = input.splitBy { it.isEmpty() }
    val wires = w.map { it.substringBefore(':') }.toMutableSet()
    val gates = g.map {
        val (left,output) = it.split(" -> ")
        val (input0, type, input1) = left.split(" ")
        wires.add(output) // to include outputs z.. in the set
        Gate(GateType.valueOf(type), input0, input1, output)
    }

    val nBits = wires.count { it.first()=='x' }
    val adderGates = adderGates(nBits)

    val outputs = wires.filter { it.first() == 'z' }.sorted()
    val checked = wires.filter { it.first() in "xy" }.toMutableSet()
    val swapped = mutableSetOf<String>()
    for (o in outputs) {
        val toAdder = mutableMapOf(o to listOf(o))
        val open = mutableListOf(o)
        while (open.isNotEmpty()) {
            val wa = open.removeAt(0)
            val ga = adderGates.first { it.output == wa }
            val wms = toAdder[wa]!!.filter { it !in checked }
            val gm = gates.firstOrNull { it.output in wms && it.type == ga.type }
            if (gm == null) {
                swapped.addAll(wms)
                continue
            }
            checked.add(gm.output)
            swapped.remove(gm.output)
            val ims = listOf(gm.input0, gm.input1).filter { it !in checked }
            if (ims.isNotEmpty())
                listOf(ga.input0, ga.input1).filter { it !in checked }
                    .forEach { toAdder[it] = ims; open.add(it) }
        }
    }
    println(swapped.sorted().joinToString(",")) // dhq,hbs,jcp,kfp,pdg,z18,z22,z27
}
