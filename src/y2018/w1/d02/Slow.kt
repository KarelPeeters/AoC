package y2018.w1.d02

fun main(args: Array<String>) {
    val lines = input.lines()

//part 1
val counts = lines.map { it.groupingBy { it }.eachCount() }
println(counts.count { 2 in it.values } * counts.count { 3 in it.values })

//part 2
for (x in lines) {
    for (y in lines) {
        if (x.zip(y).count { (a, b) -> a != b } == 1)
            println(x.zip(y).joinToString("") { (a, b) -> if (a == b) "$a" else "" })
    }
}
}