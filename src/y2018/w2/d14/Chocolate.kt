package y2018.w2.d14

fun main(args: Array<String>) {
    val n = "556061"
    val ni = n.toInt()

    val list = mutableListOf(3, 7)
    val elves = intArrayOf(0, 1)

    val nDigits = n.map { it - '0' }.reversed()

    var p1Done = false
    var p2Done = false

    fun addToList(r: Int) {
        list.add(r)
        if (!p1Done && list.size == ni + 10) {
            val last10Str = list.dropLast(list.size - ni - 10).takeLast(10).joinToString("") { "$it" }
            println("part 1: $last10Str")
            p1Done = true
        }

        if (!p2Done && nDigits.withIndex().all { (i, v) -> list[list.lastIndex - i] == v }) {
            println("part 2: ${list.size - nDigits.size}")
            p2Done = true
        }
    }

    while (!p1Done || !p2Done) {
        val new = elves.sumBy { list[it] }
        if (new >= 10)
            addToList(new / 10)
        addToList(new % 10)

        for (i in elves.indices)
            elves[i] = (elves[i] + 1 + list[elves[i]]) % list.size
    }
}