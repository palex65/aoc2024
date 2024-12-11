
data class Blocks(val from: Int, val to: Int)
typealias Disk = Array<Int>

fun main() {
    val EMPTY = -1
    /**
     * Converts the disk map in a string to a disk map of block ids.
     * Each block as an id (of the file) from 0 to n-1 or -1 if it is empty.
     */
    fun String.toDisk(): Disk {
        val disk = Array( sumOf { it - '0' } ) { EMPTY }
        var di = 0
        for ((id, idx) in (indices step 2).withIndex()) {
            for (i in 0..<this[idx]-'0') disk[di++] = id
            if (idx+1<length) di+=this[idx+1]-'0'
        }
        return disk
    }

    fun Disk.checksum(): Long = foldIndexed(0L){ idx, acc, id ->
        acc + if(id!=EMPTY) id.toLong()*idx else 0L
    }

    fun part1(disk: Disk): Long {
        var i = 0 // index of empty blocks
        var j = disk.lastIndex // index of non-empty blocks
        while (true) {
            while(disk[i] != EMPTY) i++ // find empty block
            while(disk[j] == EMPTY) j-- // find non-empty block
            if(i > j) break
            disk[i] = disk[j]
            disk[j] = -1
        }
        return disk.checksum()
    }

    fun Disk.findFile(id: Int): Blocks {
        require(id >= 0)
        var i = lastIndex
        while (this[i] != id) i--
        val end = i+1
        while (i>=0 && this[i] == id) i--
        return Blocks(i+ if(i>0)1 else 0, end)
    }

    fun Disk.findFree(spaceSize: Int): Int? {
        var i = 0
        while (i<size) {
            while (this[i] != EMPTY) i++
            val start = i
            while (i<size && this[i] == EMPTY) {
                i++
                if (i-start >= spaceSize) return start
            }
        }
        return null
    }

    fun Disk.set(from: Int, size: Int, id: Int) {
        for (i in from..<from+size) this[i] = id
    }

    fun part2(disk: Disk): Long {
        for(id  in disk.last { it != EMPTY } downTo 0) {
            val file = disk.findFile(id)
            val size = file.to-file.from
            val free = disk.findFree(size)
            if (free != null && free < file.from) {
                disk.set(free, size, id)
                disk.set(file.from, size, EMPTY)
            }
        }
        return disk.checksum()
    }

    val testInput = readInput("Day09_test")[0].toDisk()
    check(
        testInput.joinToString("") { if(it==-1) "." else it.toString() } ==
        "00...111...2...333.44.5555.6666.777.888899"
    )
    check(part1(testInput.clone()) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")[0].toDisk()
    part1(input.clone()).println() // 6320029754031
    part2(input).println() // 6347435485773
}
