
class Solution {

    private data class Point(val rowHead: Int, val columnHead: Int, val alignment: Int, val distanceFromStart: Int)

    private companion object {
        const val VERTICAL = 1
        const val HORIZONTAL = 0
        const val NOT_BLOCKED = 0
        const val NOT_POSSIBLE_TO_REACH_TARGET = -1
    }

    private var rows = 0
    private var columns = 0
    private lateinit var matrix: Array<IntArray>

    fun minimumMoves(matrix: Array<IntArray>): Int {
        rows = matrix.size
        columns = matrix[0].size
        this.matrix = matrix

        val startRowHead = 0
        val startColumnHead = 1
        val targetRowHead = rows - 1
        val targetColumnHead = columns - 1

        return findMovesToReachTarget(startRowHead, startColumnHead, targetRowHead, targetColumnHead)
    }

    private fun findMovesToReachTarget(startRowHead: Int, startColumnHead: Int, targetRowHead: Int, targetColumnHead: Int): Int {
        val queue = mutableListOf<Point>()
        queue.add(Point(startRowHead, startColumnHead, HORIZONTAL, 0))

        val visited = Array<Array<BooleanArray>>(rows) { Array<BooleanArray>(columns) { BooleanArray(2) } }
        visited[startRowHead][startColumnHead][0] = true

        while (!queue.isEmpty()) {

            val current = queue.removeFirst()
            if (current.alignment == HORIZONTAL && current.rowHead == targetRowHead && current.columnHead == targetColumnHead) {
                return current.distanceFromStart
            }

            val nextDistanceFromStart = current.distanceFromStart + 1

            val moveRight = moveRightIsPossible(current.rowHead, current.columnHead, current.alignment)
            if (moveRight && !visited[current.rowHead][current.columnHead + 1][current.alignment]) {
                visited[current.rowHead][current.columnHead + 1][current.alignment] = true
                queue.add(Point(current.rowHead, current.columnHead + 1, current.alignment, nextDistanceFromStart))
            }

            val moveDown = moveDownIsPossible(current.rowHead, current.columnHead, current.alignment)
            if (moveDown && !visited[current.rowHead + 1][current.columnHead][current.alignment]) {
                visited[current.rowHead + 1][current.columnHead][current.alignment] = true
                queue.add(Point(current.rowHead + 1, current.columnHead, current.alignment, nextDistanceFromStart))
            }

            val moveClockwise = moveClockwiseIsPossible(current.rowHead, current.columnHead, current.alignment)
            if (moveClockwise && !visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)]) {
                visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)] = true
                queue.add(Point(current.rowHead + 1, current.columnHead - 1, change(current.alignment), nextDistanceFromStart))
            }

            val moveCounterclockwise = moveCounterclockwiseIsPossible(current.rowHead, current.columnHead, current.alignment)
            if (moveCounterclockwise && !visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)]) {
                visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)] = true
                queue.add(Point(current.rowHead - 1, current.columnHead + 1, change(current.alignment), nextDistanceFromStart))
            }
        }

        return NOT_POSSIBLE_TO_REACH_TARGET
    }

    private fun moveRightIsPossible(row: Int, column: Int, alignment: Int): Boolean {
        if (alignment == HORIZONTAL && column + 1 < columns && matrix[row][column + 1] == NOT_BLOCKED) {
            return true
        }
        return (alignment == VERTICAL && column + 1 < columns && matrix[row - 1][column + 1] == NOT_BLOCKED && matrix[row][column + 1] == NOT_BLOCKED)
    }

    private fun moveDownIsPossible(row: Int, column: Int, alignment: Int): Boolean {
        if (alignment == VERTICAL && row + 1 < rows && matrix[row + 1][column] == NOT_BLOCKED) {
            return true
        }
        return alignment == HORIZONTAL && row + 1 < rows && matrix[row + 1][column - 1] == NOT_BLOCKED && matrix[row + 1][column] == NOT_BLOCKED
    }

    private fun moveClockwiseIsPossible(row: Int, column: Int, alignment: Int): Boolean {
        return alignment == HORIZONTAL && row + 1 < rows && matrix[row + 1][column] == NOT_BLOCKED && matrix[row + 1][column - 1] == NOT_BLOCKED
    }

    private fun moveCounterclockwiseIsPossible(row: Int, column: Int, alignment: Int): Boolean {
        return alignment == VERTICAL && column + 1 < columns && matrix[row][column + 1] == NOT_BLOCKED && matrix[row - 1][column + 1] == NOT_BLOCKED
    }

    private fun change(alignment: Int): Int {
        return alignment xor 1
    }
}
