package y2018.w3.d21

import readText
import y2018.w3.d16.Params
import y2018.w3.d16.opcodes

val input = readText("y2018/w3/d21/input.txt")

fun main(args: Array<String>) {
    val ipIndex = input.lines().first().substringAfter(' ').toInt()
    val program = input.lines().drop(1).map { line ->
        val (name, a, b, c) = line.split(' ')
        opcodes.first { it.name == name } to Params(a.toInt(), b.toInt(), c.toInt())
    }

    val registers = IntArray(6)
    fun ip() = registers[ipIndex]

    var prev = -1
    val seen = mutableSetOf<Int>()

    while (ip() in program.indices) {
        if (ip() == 28) {
            val e = registers[2]

            if (seen.isEmpty())
                println("part 1: $e")

            if (!seen.add(e)) {
                println("part 2: $prev")
                break
            }

            prev = e
        }

        val (code, params) = program[ip()]
        registers[params.c] = code.action(params, registers)
        registers[ipIndex]++
    }
}