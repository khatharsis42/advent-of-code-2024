import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


// Gratuitously stolen from Khatharsis repo, but tweaked for jvm Kotlin
open class DaySolver(val day: Int, val name: String) {
    open fun firstPart(input: List<String>): String = "First part isn't done yet."
    open fun secondPart(input: List<String>): String = "Second part isn't done yet."

    @OptIn(ExperimentalTime::class)
    fun solve() {
        val time = measureTime {
            println("Solving for day $day: $name")
            println("   Part 1: ${firstPart(input)}")
            println("   Part 2: ${secondPart(input)}")
        }
        println("   Time taken: $time")
    }

    @OptIn(ExperimentalTime::class)
    fun solveTest() {
        val time = measureTime {
            println("[TEST] Solving for day $day: $name")
            println("   Part 1: ${firstPart(exampleInput)}")
            println("   Part 2: ${secondPart(exampleInput)}")
        }
        println("   Time taken: $time")
    }

    open val exampleInput: List<String> = listOf()

    val input: List<String> = runBlocking {
        val f = File("inputs/$day")
        if (f.exists()) {
            f.readLines()
        } else {
            val response = client.get("https://adventofcode.com/2024/day/$day/input") {
                header("User-Agent", "hguelque@gmail.com")
            }
            val content = response.bodyAsText().trimEnd()
            f.parentFile.mkdir()
            f.writeText(content)
            content.split("\n")
        }
    }

    companion object {
        val client = HttpClient(CIO) {
            install(HttpCookies) {
                val cookie = DaySolver::class.java.getResource("cookie")?.readText()
                    ?: error("The cookie file named cookie can not be found in the resources folder")
                storage = ConstantCookiesStorage(Cookie("session", cookie, domain = "adventofcode.com"))
            }
        }
    }
}
