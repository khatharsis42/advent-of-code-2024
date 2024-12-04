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

    // List of aligned MA
    private val maList = mutableListOf<Pair<Coordinates, Coordinates>>()

    override fun firstPart(input: List<String>): String {
        // Creating the sets for each letter
        // Takes a bit of time but prevents us from having to check for edge cases later on
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

        // Check for each aligned MA
        mSet.forEach { mCoord ->
            mCoord.getNeighboursDiagonals().forEach { aCoord ->
                if (aCoord in aSet)
                    maList.add(mCoord to aCoord)
            }
        }

        // Count the number of XMAS lines
        val xmasCount = maList.count { (mCoord, aCoord) ->
            val orientation = aCoord - mCoord
            val xCoord = mCoord - orientation
            val sCoord = aCoord + orientation
            xCoord in xSet && sCoord in sSet
        }

        return xmasCount.toString()
    }


    override fun secondPart(input: List<String>): String {
        // Create a map where the key is wherever there's an A that's in the middle of an aligned MAS
        // And the value is the list of MAS going through it
        val aMap_msList = mutableMapOf<Coordinates, MutableList<Pair<Coordinates, Coordinates>>>()
        maList.forEach { (mCoord, aCoord) ->
            val orientation = aCoord - mCoord
            val sCoord = aCoord + orientation
            if (sCoord in sSet) {
                if (aMap_msList[aCoord] == null) {
                    aMap_msList[aCoord] = mutableListOf()
                }
                aMap_msList[aCoord]!!.add(mCoord to sCoord)
            }
        }

        var x_masCount = 0
        aMap_msList.forEach { aCoord, msList ->
            while (msList.isNotEmpty()) {
                val msLine = msList.first()
                msList.remove(msLine)
                val crossingMsLine = msList.find {
                    (it.second - it.first) * (msLine.second - msLine.first) == 0
                }
                if (crossingMsLine != null) {
                    msList.remove(crossingMsLine)
                    x_masCount++
                }
            }
        }

        return x_masCount.toString()
    }
}
