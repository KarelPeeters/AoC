package y2017.w3.d16

import readText

val moves = readText("y2017/w3/d16/input.txt").split(",")
val size = 16

fun main(args: Array<String>) {
    val str = "abcdefghijklmnop"

    println(str.dance())
    println((0 until 1_000_000_000).fold(str) { acc, _ -> acc.dance() })
}

val danceResults = mutableMapOf<String, String>()

fun String.dance() = danceResults.getOrPut(this) {
    var arr = this.toCharArray()
    var swap = CharArray(size)

    for (move in moves) {
        when (move[0]) {
            's' -> {
                val shift = move.substring(1).toInt()
                repeat(size) { swap[(it + shift) % size] = arr[it] }
                arr.also { arr = swap; swap = it }
            }
            'x' -> {
                val (a, b) = move.substring(1).split("/").map { it.toInt() }
                arr.swap(a, b)
            }
            'p' -> {
                val (a, b) = move.substring(1).split("/").map { it[0]}
                arr.swap(arr.indexOf(a), arr.indexOf(b))
            }
            else -> throw IllegalArgumentException()
        }
    }

    String(arr)
}

fun CharArray.swap(a: Int, b: Int) {
    val tmp = this[a]
    this[a] = this[b]
    this[b] = tmp
}