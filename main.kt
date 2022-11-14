import java.io.File
import java.util.*
import kotlin.system.exitProcess

enum class Token(val tokenName: Char) {
    DOT('.'),
    GREATER_THAN('>'),
    LESS_THAN('<'),
    PLUS('+'),
    MINUS('-'),
    COMMA(','),
    LEFT_SQUARE_BRACKET('['),
    RIGHT_SQUARE_BRACKET(']'),
}

class Interpreter(private val tokens: Array<Token>) {

    private val memorySize = 30_000
    private val memory     = IntArray(memorySize)
    private var memPos     = 0
    private var prgPos     = 0

    private fun jump(step: Int) {
        var i       = prgPos
        var loops   = 1

        while (loops > 0) {
            i += step
            when (tokens[i]) {
                Token.LEFT_SQUARE_BRACKET   -> loops += step
                Token.RIGHT_SQUARE_BRACKET  -> loops -= step
                else                        -> {}
            }
        }

        prgPos = i
    }

    fun parseTokens()
    {
        do {
            when (tokens[prgPos]) {
                Token.GREATER_THAN          -> memPos = if (memPos == memorySize - 1) 0 else memPos +1
                Token.LESS_THAN             -> memPos = if (memPos == 0) memorySize - 1 else memPos - 1
                Token.MINUS                 -> memory[memPos]--
                Token.PLUS                  -> memory[memPos]++
                Token.COMMA                 -> memory[memPos] = Scanner(System.`in`).next().single().code
                Token.DOT                   -> print(memory[memPos].toChar())
                Token.LEFT_SQUARE_BRACKET   -> if (memory[memPos] == 0) jump( 1)
                Token.RIGHT_SQUARE_BRACKET  -> if (memory[memPos] != 0) jump(-1)
            }

            prgPos++
        } while (prgPos < tokens.size)

    }
}

fun readFileDirectlyAsText(fileName: String): String =
    File(fileName)
        .readText(Charsets.UTF_8)
        .replace("\n", "")
        .replace("\t", "")
        .trim()

fun main(args: Array<String>) {
    val fileName = args.getOrNull(0)

    if (fileName == null) {
        println("Error: file path needed as first parameter")
        exitProcess(0)
    }

    val fileContentFormatted = readFileDirectlyAsText("bfPrograms/fibonacci.bf")

    var tokens = arrayOf<Token>()

    fileContentFormatted.toCharArray().forEach { char ->
        val tokenExist = Token.values().singleOrNull { it.tokenName == char }

        if (tokenExist != null) {
            tokens += tokenExist
        }

    }

    Interpreter(tokens).parseTokens()
}