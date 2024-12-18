
class CPU(
    var rA: Long=0,
    var rB: Long=0,
    var rC: Long=0
) {
    var ip = 0
    var output = mutableListOf<Int>()
    fun exec(prog: List<Int>) {
        ip = 0
        output.clear()
        while(ip < prog.size) {
            val arg = prog[ip+1]
            when (prog[ip]) {
                0 -> adv(arg); 1 -> bxl(arg); 2 -> bst(arg)
                3 -> jnz(arg); 4 -> bxc();    5 -> out(arg)
                6 -> bdv(arg); 7 -> cdv(arg)
                else -> error("Invalid opcode ${prog[ip]}")
            }
            ip+=2
        }
    }
    fun outputOfExec(prog: List<Int>): String {
        exec(prog)
        return output.joinToString(",")
    }
    fun comboOp(op: Int): Long =
        if (op in 0..3) op.toLong()
        else when(op) {
            4 -> rA; 5 -> rB; 6 -> rC
            else -> error("Invalid operand")
        }
    fun adv(op: Int) { rA /= (1 shl comboOp(op).toInt()) }
    fun bxl(op: Int) { rB = rB xor op.toLong() }
    fun bst(op: Int) { rB = comboOp(op) % 8 }
    fun jnz(op: Int) { if (rA!=0L) ip = op-2 }
    fun bxc() { rB = rB xor rC }
    fun out(op: Int) { output.add((comboOp(op)%8).toInt()) }
    fun bdv(op: Int) { rB = rA / (1 shl comboOp(op).toInt()) }
    fun cdv(op: Int) { rC = rA / (1 shl comboOp(op).toInt()) }

    fun exec(vararg prog: Int) = exec(prog.toList())
    fun outputOfExec(vararg prog: Int) = outputOfExec(prog.toList())
}

fun Lines.toRegs() = takeWhile { it.isNotEmpty() }.map { it.substringAfter(": ").toLong() }
fun Lines.toProg() = last().substringAfter(": ").split(",").map { it.toInt() }

fun main() {
/*
If register C contains 9, the program 2,6 would set register B to 1.
If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.
If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.
If register B contains 29, the program 1,7 would set register B to 26.
If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.
*/
    val c1 = CPU(rC=9); c1.exec(2,6); check(c1.rB==1L)
    val c2 = CPU(rA=10); check(c2.outputOfExec(5,0,5,1,5,4)== "0,1,2")
    val c3 = CPU(rA=2024); check(c3.outputOfExec(0,1,5,4,3,0)== "4,2,5,6,7,7,7,7,3,1,0"); check(c3.rA==0L)
    val c4 = CPU(rB=29); c4.exec(1,7); check(c4.rB==26L)
    val c5 = CPU(rB=2024, rC=43690); c5.exec(4,0); check(c5.rB==44354L)

    readInput("Day17_test").let {
        val regs = it.toRegs()
        val cpu = CPU(regs[0], regs[1], regs[2])
        check(cpu.outputOfExec(it.toProg()) == "4,6,3,5,6,3,5,2,1,0")
    }

    val input = readInput("Day17")
    val regs = input.toRegs()
    val cpu = CPU(regs[0], regs[1], regs[2])
    println(cpu.outputOfExec(input.toProg())) // 6,7,5,2,1,3,5,1,7
}
