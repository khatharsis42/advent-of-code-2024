import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.math.abs


fun <A, B>List<A>.pmap(f: suspend (A) -> B): List<B> = runBlocking {
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


fun Coordinates.rotate90() = second to -first

fun Coordinates.getNeighbours() = listOf(
    this.first + 1 to this.second,
    this.first - 1 to this.second,
    this.first to this.second + 1,
    this.first to this.second - 1
)

fun Coordinates.getNeighboursDiagonals() = listOf(
    this.first + 1 to this.second - 1,
    this.first + 1 to this.second,
    this.first + 1 to this.second + 1,
    this.first to this.second - 1,
    this.first to this.second + 1,
    this.first - 1 to this.second - 1,
    this.first - 1 to this.second,
    this.first - 1 to this.second + 1
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

operator fun <T> Coordinates.get(grid: List<List<T>>) = grid[this.first][this.second]

fun Coordinates.manhattan(other: Coordinates) = abs(this.first - other.first) + abs(this.second - other.second)
