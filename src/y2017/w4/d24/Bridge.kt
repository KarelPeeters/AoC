package y2017.w4.d24

import readText

val text = readText("y2017/w4/d24/input.txt")
val pieces = text.lines().map { it.split("/").map { it.toInt() } }.map { (a, b) -> Piece(a, b) }

fun main(args: Array<String>) {
    val emptyBridge = Bridge(0, 0, 0)

    println(maxBridgeBy(pieces, emptyBridge, compareBy(Bridge::strength)).strength)
    println(maxBridgeBy(pieces, emptyBridge, compareBy(Bridge::length) then compareBy(Bridge::strength)).strength)
}

data class Piece(val a: Int, val b: Int) {
    val strength = a + b
    fun has(v: Int) = a == v || b == v
    fun other(v: Int) = if (a == v) b else if (b == v) a else throw IllegalArgumentException()
}

data class Bridge(val length: Int, val strength: Int, val end: Int) {
    operator fun plus(piece: Piece) = Bridge(length + 1, strength + piece.strength, piece.other(end))
}

fun maxBridgeBy(pieces: List<Piece>, bridge: Bridge, comparator: Comparator<Bridge>): Bridge {
    return pieces.filter { it.has(bridge.end) }.map {
        maxBridgeBy(pieces - it, bridge + it, comparator)
    }.maxWith(comparator) ?: bridge
}