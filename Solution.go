
package main
import "container/list"

type Point struct {
    rowHead           int
    columnHead        int
    alignment         int
    distanceFromStart int
}

const VERTICAL = 1
const HORIZONTAL = 0
const NOT_BLOCKED = 0
const NOT_POSSIBLE_TO_REACH_TARGET = -1

var rows = 0
var columns = 0
var matrix [][]int

func minimumMoves(grid [][]int) int {
    rows = len(grid)
    columns = len(grid[0])
    matrix = grid

    startRowHead := 0
    startColumnHead := 1
    targetRowHead := rows - 1
    targetColumnHead := columns - 1

    return findMovesToReachTarget(startRowHead, startColumnHead, targetRowHead, targetColumnHead)
}

func findMovesToReachTarget(startRowHead int, startColumnHead int, targetRowHead int, targetColumnHead int) int {
    queue := list.New()
    queue.PushBack(Point{startRowHead, startColumnHead, HORIZONTAL, 0})

    visited := make([][][]bool, rows)
    for i := range visited {
        visited[i] = make([][]bool, columns)
        for j := range visited[i] {
            visited[i][j] = make([]bool, 2)
        }
    }
    visited[startRowHead][startColumnHead][0] = true

    for queue.Len() > 0 {

        current := queue.Front().Value.(Point)
        queue.Remove(queue.Front())
        if current.alignment == HORIZONTAL && current.rowHead == targetRowHead && current.columnHead == targetColumnHead {
            return current.distanceFromStart
        }

        nextDistanceFromStart := current.distanceFromStart + 1

        moveRight := moveRightIsPossible(current.rowHead, current.columnHead, current.alignment)
        if moveRight && !visited[current.rowHead][current.columnHead + 1][current.alignment] {
            visited[current.rowHead][current.columnHead + 1][current.alignment] = true
            queue.PushBack(Point{current.rowHead, current.columnHead + 1, current.alignment, nextDistanceFromStart})
        }

        moveDown := moveDownIsPossible(current.rowHead, current.columnHead, current.alignment)
        if moveDown && !visited[current.rowHead + 1][current.columnHead][current.alignment] {
            visited[current.rowHead + 1][current.columnHead][current.alignment] = true
            queue.PushBack(Point{current.rowHead + 1, current.columnHead, current.alignment, nextDistanceFromStart})
        }

        moveClockwise := moveClockwiseIsPossible(current.rowHead, current.columnHead, current.alignment)
        if moveClockwise && !visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)] {
            visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)] = true
            queue.PushBack(Point{current.rowHead + 1, current.columnHead - 1, change(current.alignment), nextDistanceFromStart})
        }

        moveCounterclockwise := moveCounterclockwiseIsPossible(current.rowHead, current.columnHead, current.alignment)
        if moveCounterclockwise && !visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)] {
            visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)] = true
            queue.PushBack(Point{current.rowHead - 1, current.columnHead + 1, change(current.alignment), nextDistanceFromStart})
        }
    }

    return NOT_POSSIBLE_TO_REACH_TARGET
}

func moveRightIsPossible(row int, column int, alignment int) bool {
    if alignment == HORIZONTAL && column + 1 < columns && matrix[row][column + 1] == NOT_BLOCKED {
        return true
    }
    return (alignment == VERTICAL && column + 1 < columns && matrix[row - 1][column + 1] == NOT_BLOCKED && matrix[row][column + 1] == NOT_BLOCKED)
}

func moveDownIsPossible(row int, column int, alignment int) bool {
    if alignment == VERTICAL && row+1 < rows && matrix[row + 1][column] == NOT_BLOCKED {
        return true
    }
    return alignment == HORIZONTAL && row + 1 < rows && matrix[row + 1][column - 1] == NOT_BLOCKED && matrix[row + 1][column] == NOT_BLOCKED
}

func moveClockwiseIsPossible(row int, column int, alignment int) bool {
    return alignment == HORIZONTAL && row + 1 < rows && matrix[row + 1][column] == NOT_BLOCKED && matrix[row + 1][column - 1] == NOT_BLOCKED
}

func moveCounterclockwiseIsPossible(row int, column int, alignment int) bool {
    return alignment == VERTICAL && column + 1 < columns && matrix[row][column + 1] == NOT_BLOCKED && matrix[row - 1][column + 1] == NOT_BLOCKED
}

func change(alignment int) int {
    return alignment ^ 1
}
