class Day4 : DaySolver(4, "Ceres Search") {
    override val exampleInput = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().lines()

    private val xSet = mutableSetOf<Coordinates>()
    private val mSet = mutableSetOf<Coordinates>()
    private val aSet = mutableSetOf<Coordinates>()
    private val sSet = mutableSetOf<Coordinates>()

    override fun firstPart(input: List<String>): String {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    'X' -> xSet.add(Coordinates(x, y))
                    'M' -> mSet.add(Coordinates(x, y))
                    'A' -> aSet.add(Coordinates(x, y))
                    'S' -> sSet.add(Coordinates(x, y))
                }
            }
        }

        val xmList = mutableListOf<Pair<Coordinates, Coordinates>>()
        xSet.forEach { xCoord ->
            xCoord.getNeighboursDiagonals().forEach { mCoord ->
                if (mCoord in mSet)
                    xmList.add(xCoord to mCoord)
            }
        }
        val xmasCount = xmList.count { (xCoord, mCoord) ->
            val orientation = mCoord - xCoord
            val aCoord = mCoord + orientation
            val sCoord = aCoord + orientation
            aCoord in aSet && sCoord in sSet
        }

        return xmasCount.toString()
    }


    override fun secondPart(input: List<String>): String {
        val maList = mutableListOf<Pair<Coordinates, Coordinates>>()
        mSet.forEach { mCoord ->
            mCoord.getNeighboursDiagonals().forEach { aCoord ->
                if (aCoord in aSet)
                    maList.add(mCoord to aCoord)
            }
        }
        val masList = maList.mapNotNull { (mCoord, aCoord) ->
            val orientation = aCoord - mCoord
            val sCoord = aCoord + orientation
            if (sCoord in sSet) Triple(mCoord, aCoord, sCoord) else null
        }.toMutableSet()

        var x_masCount = 0
        while (masList.isNotEmpty()) {
            val masLine = masList.first()
            masList.remove(masLine)
            val crossingMasLine = masList.find {
                it.second == masLine.second &&
                        (it.second - it.first) * (masLine.second - masLine.first) == 0
            }
            if (crossingMasLine != null) {
                masList.remove(crossingMasLine)
                x_masCount++
            }
        }



        return x_masCount.toString()
    }
}
