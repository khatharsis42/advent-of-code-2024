class Day8: DaySolver(8, "Resonant Collinearity") {
    override val exampleInput = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent().lines()

    val antennas = mutableMapOf<Char, MutableSet<Coordinates>>()

    private fun Coordinates.findSimpleAntinodes(other:  Coordinates, limits: Coordinates)= buildSet {
        val first = this@findSimpleAntinodes + this@findSimpleAntinodes - other
        val second = other + other - this@findSimpleAntinodes
        if (first.first in 0 until limits.first && first.second in 0 until limits.second)
            add(first)
        if (second.first in 0 until limits.first && second.second in 0 until limits.second)
            add(second)
    }

    private fun Coordinates.findAdvancedAntinodes(other: Coordinates, limits: Coordinates): Set<Coordinates> {
        val distance = other - this
        return buildSet {
            var temp = this@findAdvancedAntinodes - distance
            while (temp.first in 0 until limits.first && temp.second in 0 until limits.second) {
                add(temp)
                temp -= distance
            }
            temp = other + distance
            while (temp.first in 0 until limits.first && temp.second in 0 until limits.second) {
                add(temp)
                temp += distance
            }
            add(other)
            add(this@findAdvancedAntinodes)
        }
    }

    override fun firstPart(input: List<String>): String {
        val inputSize = input.size to input.first().length
        input.forEachIndexed { line, s -> s.forEachIndexed { col, c ->
            if (c != '.') {
                if (c !in antennas)
                    antennas[c] = mutableSetOf()
                antennas[c]!!.add(Coordinates(line, col))
            }
        } }
        return buildSet {
            antennas.forEach { (frequency, listAntennas) ->
                    listAntennas.forEachIndexed { firstIndex, firstAntenna ->
                        listAntennas.drop(firstIndex + 1).forEach { secondAntenna ->
                            addAll(firstAntenna.findSimpleAntinodes(secondAntenna, inputSize)
                            )
                        }
                    }
            }
        }.size.toString()
    }

    override fun secondPart(input: List<String>): String {
        val inputSize = input.size to input.first().length

        return buildSet {
            antennas.forEach { (frequency, listAntennas) ->
                listAntennas.forEachIndexed { firstIndex, firstAntenna ->
                    listAntennas.drop(firstIndex + 1).forEach { secondAntenna ->
                        addAll(firstAntenna.findAdvancedAntinodes(secondAntenna, inputSize)
                        )
                    }
                }
            }
        }.size.toString()
    }
}
