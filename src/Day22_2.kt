import kotlin.concurrent.thread

fun main() {
    val input = readInput("Day22")
//    val input = listOf("1","2","3","2024")

    val buyers = input.map { it.toInt() }
    val prices = buyers.map { generateSequence(it) { process(it) }.take(2001).map { (it % 10) }.toList() }
    val diffs = prices.map{ it.zipWithNext().map { it.second - it.first } }

    fun List<Int>.indexOfSequence(seq: List<Int>): Int {
        for(i in 0 ..< lastIndex-seq.size)
            for(j in seq.indices) {
                if( this[i+j] != seq[j] ) break
                if( j == seq.lastIndex ) return i
            }
        return -1
    }
    fun bananasForSequence(s: List<Int>): Int =
        diffs.mapIndexed { buyer, it ->
            val idx = it.indexOfSequence(s)
            if (idx == -1) 0 else prices[buyer][idx + s.size]
        }.sum()

    val r = -9..9
    val allSeqs = r.flatMap { a -> r.flatMap { b -> r.flatMap { c -> r.map { d -> listOf(a,b,c,d) } } } }
    val bestSeq = allSeqs.maxBy {
        bananasForSequence(it).also { n -> if (n > 0) println("$it -> $n") }
    }
    println(bestSeq)  // [0, -3, 2, 1]
    println(bananasForSequence(bestSeq)) // 2171 -> 2191
}
