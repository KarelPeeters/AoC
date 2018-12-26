package y2017.w3.d20

import readText
import kotlin.math.abs
import kotlin.math.sqrt

val input = readText("y2017/w3/d20/input.txt").replace("<|>".toRegex(), "").lines().map {
    it.split("(, )?[a-z]=".toRegex()).drop(1).map {
        it.split(",").map { it.toInt() }
    }.map { (x, y, z) -> Vec3(x, y, z) }
}.mapIndexed { i, (p, v, a) -> Particle(i, p, v, a) }

fun main(args: Array<String>) {
    println(firstHitTime(input[0], input[1]))

    println(input.minWith(
            compareBy({ it.a.manhattan() }, { it.v.manhattanInDir(it.a) }, { it.p.manhattanInDir(it.a) })
    )?.id)

    val hits = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
    for (a in 0 until input.size) {
        for (b in a + 1 until input.size) {
            firstHitTime(input[a], input[b])?.let { t ->
                hits.getOrPut(t) { mutableListOf() } += a to b
            }
        }
    }

    val alive = MutableList(input.size) { true }
    for ((_, hitList) in hits.entries.sortedBy { it.key }) {
        val toKill = hitList.filter { alive[it.first] && alive[it.second] }
                .flatMap { listOf(it.first, it.second) }

        toKill.forEach { alive[it] = false }
    }

    println(alive.count { it })
}

fun firstHitTime(a: Particle, b: Particle): Int? {
    val sol = solveVecQuad(
            a.a - b.a,                              //*t^2
            (a.v * 2 + a.a) - (b.v * 2 + b.a),      //*t
            (a.p - b.p) * 2                         //
    )

    return sol.sorted().firstOrNull { it >= 0 }
}

fun solveVecQuad(a: Vec3, b: Vec3, c: Vec3): List<Int> {
    val xSol = solveQuad(a.x, b.x, c.x)
    val ySol = solveQuad(a.y, b.y, c.y)
    val zSol = solveQuad(a.z, b.z, c.z)

    val sols = (xSol ?: emptyList()) + (ySol ?: emptyList()) + (zSol ?: emptyList())

    return sols.filter {
        xSol?.contains(it) ?: true &&
                ySol?.contains(it) ?: true &&
                zSol?.contains(it) ?: true
    }
}

fun solveQuad(a: Int, b: Int, c: Int): List<Int>? {
    val d = b * b - 4 * a * c
    return when {
        a == 0 -> when (b) {
            0 -> null //not meaningful, it isn't even a linear equation
            else -> listOf(-c.toFloat() / b)
        }
        d > 0 -> listOf((-b + sqrt(d.toFloat())) / (2 * a), (-b - sqrt(d.toFloat())) / (2 * a))
        d == 0 -> listOf(-b / (2 * a).toFloat())
        else -> emptyList()
    }?.map { it.toInt() }?.filter { a * it * it + b * it + c == 0 }
}

data class Vec3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Vec3) = Vec3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vec3) = Vec3(x - other.x, y - other.y, z - other.z)
    operator fun times(other: Int) = Vec3(x * other, y * other, z * other)

    fun manhattan() = abs(x) + abs(y) + abs(z)
    fun manhattanInDir(dir: Vec3) = listOf(x to dir.x, y to dir.y, z to dir.z).sumBy { (v, d) ->
        when {
            d < 0 -> -v
            d == 0 -> abs(v)
            else -> v
        }
    }
}

data class Particle(val id: Int, var p: Vec3, var v: Vec3, var a: Vec3)