package y2017.w3.d15

fun main(args: Array<String>) {
    val rawA = generateSequence(703L) { it * 16807 % 2147483647 }
    val rawB = generateSequence(516L) { it * 48271 % 2147483647 }

    println(
            rawA.zip(rawB)
                    .take(40_000_000)
                    .count { (a, b) -> a and 0xffff == b and 0xffff }
    )

    println(
            rawA.filter { it % 4 == 0L }.zip(rawB.filter { it % 8 == 0L })
                    .take(5_000_000)
                    .count { (a, b) -> a and 0xffff == b and 0xffff }
    )

}