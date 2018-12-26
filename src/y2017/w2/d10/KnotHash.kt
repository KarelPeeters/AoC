package y2017.w2.d10

val text = "31,2,85,1,80,109,35,63,98,255,0,13,105,254,128,33"
val size = 256

fun main(args: Array<String>) {
    println(
            sparseKnotHash((if (text.isEmpty()) listOf() else text.split(",")).map { it.toInt() }, rounds = 1)
                    .let { it[0] * it[1] }
    )

    println(
            knotHash(text)
    )
}

fun knotHash(text: String) = sparseKnotHash(text.map { it.toInt() } + listOf(17, 31, 73, 47, 23), rounds = 64)
        .chunked(16).map { it.reduce { a, b -> a xor b } }
        .joinToString("") { "%02x".format(it) }

fun sparseKnotHash(lengths: List<Int>, rounds: Int): List<Int> {
    val list = CircularList(List(size) { it })

    var curr = 0
    var skip = 0

    repeat(rounds) {
        println(list.normalList)

        for (length in lengths) {
            list.reverse(curr, length)
            curr += length + skip
            skip++
        }
    }

    return list.normalList
}

class CircularList<T>(list: List<T>) {
    private val list = list.toMutableList()
    val normalList get() = list.toList()

    operator fun get(index: Int) = list[Math.floorMod(index, list.size)]
    operator fun set(index: Int, value: T) {
        list[Math.floorMod(index, list.size)] = value
    }

    fun reverse(from: Int, length: Int) {
        for (i in 0 until length / 2) {
            val a = from + i
            val b = from + length - i - 1

            this[a].also {
                this[a] = this[b]
                this[b] = it
            }
        }
    }

    override fun toString() = list.toString()
}