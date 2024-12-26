
enum class GateType { AND, OR, XOR }
enum class WireValue(val value: Char) { ONE('1'), ZERO('0'), NONE('?') }
data class Gate(val type: GateType, val input0: String, val input1: String, val output: String)

fun main() {
//    val input = readInput("Day24_test")
    val input = readInput("Day24")
    val (w,g) = input.splitBy { it.isEmpty() }
    val wires = w.associateTo(mutableMapOf()) {
        val (name, value) = it.split(": ")
        name to if(value=="1") WireValue.ONE else WireValue.ZERO
    }
    fun addWire(name: String, value: WireValue=WireValue.NONE) {
        if (wires.containsKey(name)) return
        wires[name] = value
    }
    val gates = g.map {
        val (left,output) = it.split(" -> ")
        val (input0, type, input1) = left.split(" ")
        addWire(output)
        addWire(input0)
        addWire(input1)
        Gate(GateType.valueOf(type), input0, input1, output)
    }
    fun getWireValue(wire: String): WireValue {
        val value = checkNotNull(wires[wire])
        if (value != WireValue.NONE) return value
        val gate = gates.first { it.output == wire }
        val input0 = getWireValue(gate.input0)
        var res = WireValue.NONE
        if (gate.type == GateType.AND && input0 == WireValue.ZERO) res = WireValue.ZERO
        if (gate.type == GateType.OR && input0 == WireValue.ONE) res = WireValue.ONE
        if (res == WireValue.NONE) {
            val input1 = getWireValue(gate.input1)
            res = when(gate.type) {
                GateType.AND -> if (input1 == WireValue.ONE) WireValue.ONE else WireValue.ZERO
                GateType.OR -> if (input1 == WireValue.ONE) WireValue.ONE else WireValue.ZERO
                GateType.XOR -> if (input0 != input1) WireValue.ONE else WireValue.ZERO
            }
        }
        return res.also { wires[wire] = it }
    }
    fun getNumber(start: Char) = wires.keys
        .filter { it.first()==start }.sorted()
        .map { getWireValue(it).value }.reversed()
        .joinToString("").toLong(2)
    println(getNumber('z')) // 69201640933606
}
