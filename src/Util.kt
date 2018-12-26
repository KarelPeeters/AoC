import java.io.File

internal object Resources

fun readText(path: String) = File(Resources::class.java.getResource(path).file).readText().replace("\r\n", "\n")

inline fun timeMili(n: Int = 1, block: () -> Unit) {
    repeat(n) {
        val start = System.currentTimeMillis()
        block()
        println("Took ${System.currentTimeMillis() - start}ms")
    }
}

inline fun timeNano(n: Int = 1, block: () -> Unit) {
    repeat(n) {
        val start = System.nanoTime()
        block()
        println("Took ${System.nanoTime() - start}ns")
    }
}