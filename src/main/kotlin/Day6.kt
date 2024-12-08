class Day6 : DaySolver(6, "Guard Gallivant") {
    override val exampleInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().lines()

    val walls = mutableSetOf<Coordinates>()
    var startingCoordinates = Coordinates(0, 0)


    override fun firstPart(input: List<String>): String {
        input.forEachIndexed { line, s ->
            s.forEachIndexed { col, c ->
                if (c == '#') walls.add(Coordinates(line, col))
                else if (c == '^') startingCoordinates = Coordinates(line, col)
            }
        }
        val bounds = input.size to input.first().length

        return checkStuckInLoop(bounds, walls).first.toString()
    }


    fun checkStuckInLoop(bounds: Coordinates,
                         walls:Set<Coordinates>
                          ): Pair<Int, Boolean> {
        var coordinates = startingCoordinates
        var orientation = Coordinates(-1, 0)
        val visitedCells = mutableSetOf<Coordinates>()
        val visitedCellsPosition =
            (0 until bounds.first).map { arrayOfNulls<MutableList<Coordinates>>(bounds.second).toMutableList() }
        while (coordinates.first in 0 until bounds.first && coordinates.second in 0 until bounds.second) {
            // Check if we've been in the same place, same state before
            if (coordinates in visitedCells) {
                if (orientation in coordinates[visitedCellsPosition]!!) {
                    return 0 to true
                }
                coordinates[visitedCellsPosition]!!.add(orientation)
            } else {
                visitedCells.add(coordinates)
                visitedCellsPosition[coordinates.first][coordinates.second] = mutableListOf(orientation)
            }
            // Handle the case where there's an obstacle
            if (coordinates + orientation in walls) {
                orientation = orientation.rotate90()
            }
            else {
                coordinates += orientation
            }
        }
        return visitedCells.size to false
    }

    override fun secondPart(input: List<String>): String {
        var coordinates = startingCoordinates
        var guardOrientation = Coordinates(-1, 0)
        val bounds = input.size to input.first().length
        val possibleObstructions = mutableSetOf<Coordinates>()
        while (coordinates.first in 0 until bounds.first && coordinates.second in 0 until bounds.first) {
            val futurePosition = coordinates + guardOrientation
            if (futurePosition !in walls
                && futurePosition !in possibleObstructions
                && futurePosition.first in 0 until bounds.first
                && futurePosition.second in 0 until bounds.second
                && checkStuckInLoop(bounds, walls + futurePosition).second) {
                possibleObstructions.add(futurePosition)
            }
            // Handle the case where there's an obstacle
            if (futurePosition in walls) {
                guardOrientation = guardOrientation.rotate90()
            } else {
                coordinates = futurePosition
            }
        }

        return possibleObstructions.size.toString()
    }
}
