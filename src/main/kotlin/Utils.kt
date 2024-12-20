import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.math.abs


fun Long.pow(p: Long): Long = if (p == 0L) 1L else (this * pow(p-1))

fun <A, B> List<A>.pmap(f: suspend (A) -> B): List<B> = runBlocking {
    map { async(Dispatchers.Default) { f(it) } }.map { it.await() }
}

fun List<String>.groupBySeparatorBlank() = this.fold(mutableListOf(mutableListOf<String>())) { acc, it ->
    if (it.isBlank()) {
        acc.add(mutableListOf<String>())
        return@fold acc
    } else {
        acc.last().add(it)
        return@fold acc
    }
}

fun <E> List<List<E>>.rotate(): List<List<E>> =
    this[0].indices.map { i ->
        (this.indices).map { j ->
            this[j][i]
        }
    }
typealias Coordinates = Pair<Int, Int>
typealias Coordinates3D = Triple<Int, Int, Int>


sealed class Direction {
    fun rotate90() = when (this) {
        North -> East
        East -> South
        South -> West
        West -> North
    }

    fun rotateOpp90() = when (this) {
        North -> West
        West -> South
        South -> East
        East -> North
    }

    override fun toString() = when (this) {
        North -> "North"
        West -> "West"
        East -> "East"
        South -> "South"
    }
}

object North : Direction()
object South : Direction()
object East : Direction()
object West : Direction()

operator fun Coordinates.plus(other: Direction): Coordinates = when (other) {
    North -> first - 1 to second
    South -> first + 1 to second
    East -> first to second + 1
    West -> first to second - 1
}


fun Coordinates.rotate90() = second to -first

fun Coordinates.getNeighbours() = listOf(
    this + North,
    this + East,
    this + South,
    this + West
)


fun Coordinates.getNeighboursDiagonals() = listOf(
    this + North + West,
    this + North,
    this + North + East,
    this + East,
    this + South + East,
    this + South,
    this + South + West,
    this + West
)

fun Coordinates3D.getNeighbours() = listOf(
    Triple(first, second, third + 1),
    Triple(first, second, third - 1),
    Triple(first, second + 1, third),
    Triple(first, second - 1, third),
    Triple(first + 1, second, third),
    Triple(first - 1, second, third)
)

fun IntRange.size() = last - first + 1

fun <T> Coordinates.getNeighbours(grid: List<List<T>>) = getNeighbours()
    .filter { it.first >= 0 && it.second >= 0 && it.first < grid.size && it.second < grid[0].size }

operator fun Coordinates.plus(other: Coordinates) = this.first + other.first to this.second + other.second
operator fun Coordinates.minus(other: Coordinates) = this.first - other.first to this.second - other.second
operator fun Coordinates.times(other: Coordinates) = this.first * other.second + this.second * other.first
operator fun Coordinates.times(other: Int) = (this.first * other) to (this.second * other)
operator fun Coordinates.rem(other: Coordinates) = this.first.mod(other.first) to this.second.mod(other.second)

operator fun <T> List<List<T>>.get(coordinates: Coordinates) = get(coordinates.first).get(coordinates.second)
operator fun <T> List<MutableList<T>>.set(coordinates: Coordinates, value: T) = get(coordinates.first).set(coordinates.second, value)
fun Coordinates.manhattan(other: Coordinates) = abs(this.first - other.first) + abs(this.second - other.second)
