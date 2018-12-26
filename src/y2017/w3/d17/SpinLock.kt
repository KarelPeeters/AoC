package y2017.w3.d17

val input = 314

fun main(args: Array<String>) {
    println(valueAfter(2017, iterations = 2017))
    println(valueAfterZero(iterations = 50_000_000))
}

fun valueAfter(value: Int, iterations: Int): Int {
    val list = mutableListOf(0)

    var pos = 0
    for (i in 1..iterations) {
        pos = (pos + input) % list.size + 1
        list.add(pos, i)
    }

    return (list + list[0]).zipWithNext().first { it.first == value }.second
}

fun valueAfterZero(iterations: Int): Int {
    var value = -1
    var pos = 0
    for (i in 1..iterations) {
        pos = (pos + input) % i + 1

        if (pos == 1) value = i
    }
    return value
}