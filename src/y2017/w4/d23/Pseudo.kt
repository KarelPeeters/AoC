package y2017.w4.d23

import timeMili

fun main(args: Array<String>) {
    timeMili { println((109900..126900 step 17).count { !it.toBigInteger().isProbablePrime(Int.MAX_VALUE)}) }
    timeMili { println((109900..126900 step 17).count { !it.toBigInteger().isProbablePrime(10)}) }
    timeMili { println((109900..126900 step 17).count { !it.toBigInteger().isProbablePrime(Int.MAX_VALUE)}) }
    timeMili { println((109900..126900 step 17).count { !it.toBigInteger().isProbablePrime(10)}) }
}

fun prgm() {
    var b = 109900
    val c = 126900
    var h = 0

    while (true) {
        var f = 1

        do {
            var d = 2
            var e = 2
            do {
                if (d*e == b)
                    f = 0
                e++
            } while (e != b)
            d++
        } while (d != b)

        if (f == 0)
            h++

        if (b == c)
            return
        b += 17
    }
}