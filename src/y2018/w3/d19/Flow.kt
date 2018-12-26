package y2018.w3.d19

import readText

val input = readText("y2018/w3/d19/input.txt")

fun readableCode(input: String): String {
    val pointerIndex = input.lineSequence().first().substringAfter(" ").toInt()
    val names = mutableListOf("A", "B", "C", "D", "E")
            .apply { add(pointerIndex, "ip-") } as List<String>

    val result = StringBuilder()

    for ((i, line) in input.lineSequence().drop(1).withIndex()) {
        val (name, aStr, bStr, cStr) = line.split(" ")
        val (a, b, c) = listOf(aStr, bStr, cStr).map(String::toInt)

        val bodyStr = when (name) {
            "addr" -> "${names[a]} + ${names[b]}"
            "addi" -> "${names[a]} + $b"
            "mulr" -> "${names[a]} * ${names[b]}"
            "muli" -> "${names[a]} * $b"
            "seti" -> "$a"
            "setr" -> names[a]
            "eqrr" -> "${names[a]} == ${names[b]}"
            "eqri" -> "${names[a]} == $b"
            "gtrr" -> "${names[a]} > ${names[b]}"
            "gtir" -> "$a > ${names[b]}"
            "bani" -> "${names[a]} & ${b.toString(2)}"
            "bori" -> "${names[a]} | ${b.toString(2)}"
            else -> error(name)
        }.replace("-", "")

        result.appendln("$i:".padEnd(5) + "${names[c]} = $bodyStr")
    }

     return result.toString()
}

fun sumFactorsOf(c: Int): Int {
    var a = 0
    var b = 1
    do {
        if (c % b == 0)
            a += b
        b++
    } while (b <= c)
    return a
}

fun calcC(a: Boolean): Int {
    var c = 11 * 19 * 2 * 2
    var e = 18 + 22 * 2

    c += e
    if (a) {
        e = 32 * 14 * 30 * (29 + 28 * 27)
        c += e
    }

    return c
}

fun main(args: Array<String>) {
    println(readableCode(input))

    println(sumFactorsOf(calcC(false)))
    println(sumFactorsOf(calcC(true)))
}