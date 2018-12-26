package y2017.w1.d06

val text = "5\t1\t10\t0\t1\t7\t13\t14\t3\t12\t8\t10\t7\t12\t0\t6"

fun main(args: Array<String>) {
    val input = text.split("\t").map(String::toInt)

    val visited = mutableMapOf<Int, List<Int>>()
    var current = input
    var count = 0

    while (current !in visited.values) {
        visited[count] = current
        count++

        val (maxIndex, maxValue) = current.mapIndexed { index, n -> index to n }
                .maxBy { it.second }!!

        val size = current.size
        current = current.mapIndexed { index, value ->
            val base = if (index == maxIndex) 0 else value
            val average = maxValue / size
            val added = if ((index - maxIndex + size - 1) % size < maxValue % size) 1 else 0

            base + average + added
        }
    }

    val entry = visited.entries.find { it.value == current }!!
    println(count)
    println("${count - entry.key}")
}