class Day11 : DaySolver(11, "Plutonian Pebble") {
    override val exampleInput = listOf("125 17")

    lateinit var stoneMap: Map<Long, Long>
    fun MutableMap<Long, Long>.insertStone(stone: Long, multiplier: Long = 1) {
        this[stone] = multiplier + (this[stone] ?: 0)
    }

    private fun blinkTreeList(n: Int) {
        for (loop in 1..n) {
            stoneMap = buildMap {
                stoneMap.forEach { (stone, mult) ->
                    if (stone == 0L) {
                        insertStone(1, mult)
                        return@forEach
                    }
                    val stoneLength = stone.toString().length
                    if (stoneLength % 2 == 0) {
                        insertStone(stone.toString().substring(0, stoneLength / 2).toLong(), mult)
                        insertStone(stone.toString().substring(stoneLength / 2).toLong(), mult)
                    } else
                        insertStone(stone * 2024, mult)

                }
            }
        }
    }


    override fun firstPart(input: List<String>): String {
        stoneMap = input.first().split(" ").associate { it.toLong() to 1L }
        blinkTreeList(25)
        return stoneMap.values.sumOf { it }.toString()
    }

    override fun secondPart(input: List<String>): String {
        blinkTreeList(75 - 25)
        return stoneMap.values.sumOf { it }.toString()
    }
}
