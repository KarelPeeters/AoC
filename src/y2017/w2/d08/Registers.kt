package y2017.w2.d08

import readText

val text = readText("y2017/w2/d08/input.txt")

val name = """[a-z]+"""
val number = """-?\d+"""
val regex = """($name) (inc|dec) ($number) if ($name) ([><=!]+) ($number)""".toRegex()

fun main(args: Array<String>) {
    val registers = object {
        private val map = mutableMapOf<String, Int>()

        operator fun get(register: String) = map.getOrDefault(register, 0)
        operator fun set(register: String, value: Int) = map.set(register, value)

        fun maxValue() = map.values.max() ?: 0
    }

    text.lines().map {
        val match = regex.matchEntire(it)!!
        val (reg, op, valueStr, test, comp, constStr) = match.groups.toList().drop(1).map { it!!.value }

        val delta = (if (op == "inc") 1 else -1) * valueStr.toInt()
        val value = constStr.toInt()

        registers[test].also {
            if (when (comp) {
                "<" -> it < value
                "<=" -> it <= value
                "==" -> it == value
                "!=" -> it != value
                ">=" -> it >= value
                ">" -> it > value
                else -> throw IllegalArgumentException()
            }) {
                registers[reg] += delta
            }
        }

        registers.maxValue()
    }.max().also { println(it) }

    println(registers.maxValue())
}

operator fun <T> List<T>.component6() = get(5)