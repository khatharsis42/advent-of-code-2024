import kotlin.math.min

class Day19 : DaySolver(19, "Linen Layout") {
    override val exampleInput = """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent().lines()

    private val knownPatterns = mutableMapOf<String, Boolean>()
    private val knownPatternCounts = mutableMapOf<String, Long>("" to 1L)
    private lateinit var basePatterns: Set<String>
    private val minSize: Int by lazy { basePatterns.minOf { it.length } }
    private val maxSize: Int by lazy { basePatterns.maxOf { it.length } }


    private fun String.isPatternAvailable(): Boolean = knownPatterns.getOrPut(this) {
        length > minSize && (1 until length).any {
            substring(0, it).isPatternAvailable() && substring(it).isPatternAvailable()
        }
    }

    private fun String.getPossibleDecompositions(): Long = knownPatternCounts.getOrPut(this) {
            (1..(min(maxSize, length))).sumOf {
                if (substring(0, it) in basePatterns) {
                    substring(it).getPossibleDecompositions()
                } else 0
            }
        }


    override fun firstPart(input: List<String>): String {
        basePatterns = input.first().split(", ").toSet()
        knownPatterns.putAll(basePatterns.associateWith { true })
        val demandedPatterns = input.subList(2, input.size)
        return demandedPatterns
            .map { it.isPatternAvailable() }
            .count { it }.toString()
    }

    override fun secondPart(input: List<String>): String {
        val demandedPatterns = input.subList(2, input.size)
        return demandedPatterns
            .filter { it.isPatternAvailable() }
            .map { it.getPossibleDecompositions() }
            .sumOf { it }.toString()
    }
}
