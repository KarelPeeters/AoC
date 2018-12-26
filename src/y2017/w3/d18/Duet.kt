package y2017.w3.d18

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.LinkedListChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.SendChannel
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeoutOrNull
import readText

/*val text = """set a 1
add a 2
mul a a
mod a 5
snd a
set a 0
rcv a
jgz a -1
set a 1
jgz a -2"""*/

val text = readText("y2017/w3/d18/input.txt")

val code = text.lines()
val valueRegex = "-?[0-9]+|[a-z]"
val regex = "([a-z]+) ($valueRegex) ?($valueRegex)?".toRegex()

    fun main(args: Array<String>) {
    //part 1
    //...

    //part 2
    val chan01 = LinkedListChannel<Long>()
    val chan10 = LinkedListChannel<Long>()

    runBlocking {
        async { runProgram(0, chan01, chan10) }
        async { runProgram(1, chan10, chan01) }.await().also { println(it) }
    }
}

suspend fun runProgram(id: Long, sender: SendChannel<Long>, receiver: ReceiveChannel<Long>): Int {
    val registers = Registers().apply { this["p"] = id }

    var sendCount = 0
    var pointer = 0L

    while (pointer < code.size) {
        val match = regex.matchEntire(code[pointer.toInt()])!!

        val instr = match.groups[1]!!.value
        val reg = match.groups[2]!!.value
        val num = match.groups[3]?.value?.value(registers) ?: -1L

        when (instr) {
            "set" -> registers[reg] = num
            "add" -> registers[reg] += num
            "mul" -> registers[reg] *= num
            "mod" -> registers[reg] %= num
            "snd" -> {
                sender.send(registers[reg]); sendCount++
            }
            "rcv" -> {
                registers[reg] = withTimeoutOrNull(1000) { receiver.receive() } ?: return sendCount
            }
            "jgz" -> if (reg.value(registers) > 0) pointer += num - 1
        }

        pointer++
    }

    throw IllegalStateException()
}

class Registers {
    private val map = mutableMapOf<String, Long>()

    operator fun get(register: String) = map.getOrDefault(register, 0L)
    operator fun set(register: String, value: Long) = map.set(register, value)
}

fun String.value(registers: Registers) = toLongOrNull() ?: registers[this]