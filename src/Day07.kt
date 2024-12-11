
data class Equation(val test: Long, val values: List<Long>)
typealias Equations = List<Equation>

operator fun Long.plus(l: List<Long>) = listOf(this) + l

fun main() {
    fun Lines.toEquations(): Equations = map { line ->
        val elems = line.split(' ')
        Equation(
            test = elems[0].substringBefore(':').toLong(),
            values = elems.drop(1).map { it.toLong() }
        )
    }

    fun Equation.isTrue(vararg ops: (Long,Long)->Long ): Boolean =
        if (values.size == 1) values[0]==test
        else {
            val (first, second) = values
            val others = values.drop(2)
            ops.any{ op -> Equation(test, op(first,second) + others).isTrue(*ops) }
        }

    fun add(a: Long, b: Long) = a + b
    fun multiply(a: Long, b: Long) = a * b

    fun part1(eqs: Equations): Long = eqs
        .filter { it.isTrue(::add, ::multiply) }
        .sumOf { it.test }

    fun concat(a: Long, b:Long) = "$a$b".toLong()

    fun part2(eqs: Equations): Long = eqs
        .filter { it.isTrue(::add, ::multiply, ::concat) }
        .sumOf { it.test }

    val testInput = readInput("Day07_test").toEquations()
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07").toEquations()
    part1(input).println() // 1289579105366
    part2(input).println() // 92148721834692
}
