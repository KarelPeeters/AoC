package y2017.w4.d23

import readText

val text = readText("y2017/w4/d23/input.txt")

val code = text.lines()
val valueRegex = "-?[0-9]+|[a-z]"
val regex = "([a-z]+) ($valueRegex) ?($valueRegex)?".toRegex()

fun main(args: Array<String>) {
    println(runProgram())
}

fun runProgram(): Int {
    val registers = Registers()//.apply { set("a", 1) }

    var mulCount = 0
    var pointer = 0L

    while (pointer < code.size) {
        val match = regex.matchEntire(code[pointer.toInt()])!!

        val instr = match.groups[1]!!.value
        val reg = match.groups[2]!!.value
        val num = match.groups[3]?.value?.value(registers) ?: -1L

        when (instr) {
            "set" -> registers[reg] = num
            "sub" -> registers[reg] -= num
            "mul" -> {
                registers[reg] *= num
                mulCount++
            }
            "jnz" -> if (reg.value(registers) != 0L) pointer += num - 1
        }

        pointer++
    }

    return mulCount
}

class Registers {
    private val map = mutableMapOf<String, Long>()

    operator fun get(register: String) = map.getOrDefault(register, 0L)
    operator fun set(register: String, value: Long) = map.set(register, value)
}

fun String.value(registers: Registers) = toLongOrNull() ?: registers[this]