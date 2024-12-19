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
    private val knownPatternCounts = mutableMapOf<String, Long>()
    private lateinit var basePatterns: Set<String>
    private val minSize: Int by lazy { basePatterns.minOf { it.length } }
    private val maxSize: Int by lazy { basePatterns.maxOf { it.length } }


    private fun String.isPatternAvailable(): Boolean =
        if (this in knownPatterns) knownPatterns[this]!! else
            (length > minSize && (1 until length).any {
                substring(0, it).isPatternAvailable() && substring(it).isPatternAvailable()
            }).also { knownPatterns[this] = it }

    private fun String.getPossibleDecompositions(): Long {
        if (isEmpty()) return 1L
        if (this !in knownPatternCounts) {
            val temp = (1..(min(maxSize, length))).sumOf {
                val firstSub = substring(0, it)
                val lastSub = substring(it)
                if (firstSub in basePatterns) {
                    lastSub.getPossibleDecompositions()
                } else 0
            }
            knownPatternCounts[this] = temp
        }
        return knownPatternCounts[this]!!
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
