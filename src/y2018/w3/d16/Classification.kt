package y2018.w3.d16

import readText
import java.util.*

val input = readText("y2018/w3/d16/input.txt")

data class Params(val a: Int, val b: Int, val c: Int)

class OpCode(val name: String, val action: Params.(IntArray) -> Int)

val opcodes = listOf(
        OpCode("addr") { r -> r[a] + r[b] },
        OpCode("addi") { r -> r[a] + b },
        OpCode("mulr") { r -> r[a] * r[b] },
        OpCode("muli") { r -> r[a] * b },
        OpCode("banr") { r -> r[a] and r[b] },
        OpCode("bani") { r -> r[a] and b },
        OpCode("borr") { r -> r[a] or r[b] },
        OpCode("bori") { r -> r[a] or b },
        OpCode("setr") { r -> r[a] },
        OpCode("seti") { r -> a },
        OpCode("gtir") { r -> if (a > r[b]) 1 else 0 },
        OpCode("gtri") { r -> if (r[a] > b) 1 else 0 },
        OpCode("gtrr") { r -> if (r[a] > r[b]) 1 else 0 },
        OpCode("eqir") { r -> if (a == r[b]) 1 else 0 },
        OpCode("eqri") { r -> if (r[a] == b) 1 else 0 },
        OpCode("eqrr") { r -> if (r[a] == r[b]) 1 else 0 }
)

val instrRegex = """(\d+) (\d+) (\d+) (\d+)""".toRegex()

val sampleRegex = """
Before: \[(\d+), (\d+), (\d+), (\d+)]
(\d+) (\d+) (\d+) (\d+)
After:  \[(\d+), (\d+), (\d+), (\d+)]
""".trimIndent().toRegex()

fun main(args: Array<String>) {
    val meanings = Array(opcodes.size) { opcodes.toMutableSet() }
    var manyPossibleCount = 0
    var lastMatch: MatchResult? = null

    //remove direct meanings
    for (match in sampleRegex.findAll(input)) {
        val ints = match.groupValues.drop(1).map { it.toInt() }
        val before = ints.subList(0, 4).toIntArray()
        val type = ints[4]
        val params = Params(ints[5], ints[6], ints[7])
        val after = ints.subList(8, 12).toIntArray()

        var possibleCount = 0
        for (code in opcodes) {
            val result = before.copyOf()
            result[params.c] = code.action(params, result)
            if (Arrays.equals(result, after)) {
                possibleCount++
            } else {
                meanings[type].remove(code)
            }
        }

        lastMatch = match
        if (possibleCount >= 3)
            manyPossibleCount++
    }

    //remove known meanings from others
    do {
        var changed = false
        for ((i, possible) in meanings.withIndex())
            if (possible.size == 1)
                for (j in meanings.indices - i)
                    changed = changed || meanings[j].remove(possible.first())
    } while (changed)


    //run program
    val registers = IntArray(4)
    for (match in instrRegex.findAll(input, lastMatch?.range?.endInclusive ?: 0)) {
        val (type, a, b, c) = match.groupValues.drop(1).map { it.toInt() }
        val params = Params(a, b, c)
        registers[c] = meanings[type].first().action(params, registers)
    }

    println("part 1: $manyPossibleCount")
    println("part 2: ${registers[0]}")
}