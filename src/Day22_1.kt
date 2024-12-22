
fun mix(value: Int, secret: Int): Int = value xor secret
fun prune(value: Int): Int = value.mod(16777216)

fun mixPrune(value: Int, secret: Int): Int = (value xor secret).mod(16777216)

fun process(secret: Int): Int {
    var s = mixPrune(secret * 64, secret)
    s = mixPrune(s / 32, s)
    s = mixPrune(s * 2048, s)
    return s
}

fun main() {
//    val input = readInput("Day22_test")
    val input = readInput("Day22")

    check( prune(100000000) == 16113920)
    check( mix(15, 42) == 37 )
    val seq = generateSequence(123) { process(it) }.drop(1)
    val expect = listOf(15887950, 16495136, 527345, 704524, 1553684, 12683156, 11100544, 12249484, 7753432, 5908254)
    check( seq.take(expect.size).toList() == expect )

    val buyers = input.map { it.toInt() }
    val day1 = buyers.map { var s = it; repeat(2000) { s = process(s) }; s }
    println(day1.sumOf { it.toLong() }) // 20332089158
}
