
import java.util.LinkedList;
import java.util.Queue;

public class Solution {

    private record Point(int rowHead, int columnHead, int alignment, int distanceFromStart) {}

    private static final int VERTICAL = 1;
    private static final int HORIZONTAL = 0;
    private static final int NOT_BLOCKED = 0;
    private static final int NOT_POSSIBLE_TO_REACH_TARGET = -1;

    private int rows;
    private int columns;
    private int[][] matrix;

    public int minimumMoves(int[][] matrix) {
        rows = matrix.length;
        columns = matrix[0].length;
        this.matrix = matrix;

        int startRowHead = 0;
        int startColumnHead = 1;
        int targetRowHead = rows - 1;
        int targetColumnHead = columns - 1;

        return findMovesToReachTarget(startRowHead, startColumnHead, targetRowHead, targetColumnHead);
    }

    private int findMovesToReachTarget(int startRowHead, int startColumnHead, int targetRowHead, int targetColumnHead) {
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(startRowHead, startColumnHead, HORIZONTAL, 0));

        boolean[][][] visited = new boolean[rows][columns][2];
        visited[startRowHead][startColumnHead][0] = true;

        while (!queue.isEmpty()) {

            Point current = queue.poll();
            if (current.alignment == HORIZONTAL && current.rowHead == targetRowHead && current.columnHead == targetColumnHead) {
                return current.distanceFromStart;
            }

            int nextDistanceFromStart = current.distanceFromStart + 1;

            boolean moveRight = moveRightIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveRight && !visited[current.rowHead][current.columnHead + 1][current.alignment]) {
                visited[current.rowHead][current.columnHead + 1][current.alignment] = true;
                queue.add(new Point(current.rowHead, current.columnHead + 1, current.alignment, nextDistanceFromStart));
            }

            boolean moveDown = moveDownIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveDown && !visited[current.rowHead + 1][current.columnHead][current.alignment]) {
                visited[current.rowHead + 1][current.columnHead][current.alignment] = true;
                queue.add(new Point(current.rowHead + 1, current.columnHead, current.alignment, nextDistanceFromStart));
            }

            boolean moveClockwise = moveClockwiseIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveClockwise && !visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)]) {
                visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)] = true;
                queue.add(new Point(current.rowHead + 1, current.columnHead - 1, change(current.alignment), nextDistanceFromStart));
            }

            boolean moveCounterclockwise = moveCounterclockwiseIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveCounterclockwise && !visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)]) {
                visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)] = true;
                queue.add(new Point(current.rowHead - 1, current.columnHead + 1, change(current.alignment), nextDistanceFromStart));
            }
        }

        return NOT_POSSIBLE_TO_REACH_TARGET;
    }

    private boolean moveRightIsPossible(int row, int column, int alignment) {
        if (alignment == HORIZONTAL && column + 1 < columns && matrix[row][column + 1] == NOT_BLOCKED) {
            return true;
        }
        return (alignment == VERTICAL && column + 1 < columns && matrix[row - 1][column + 1] == NOT_BLOCKED && matrix[row][column + 1] == NOT_BLOCKED);
    }

    private boolean moveDownIsPossible(int row, int column, int alignment) {
        if (alignment == VERTICAL && row + 1 < rows && matrix[row + 1][column] == NOT_BLOCKED) {
            return true;
        }
        return alignment == HORIZONTAL && row + 1 < rows && matrix[row + 1][column - 1] == NOT_BLOCKED && matrix[row + 1][column] == NOT_BLOCKED;
    }

    private boolean moveClockwiseIsPossible(int row, int column, int alignment) {
        return alignment == HORIZONTAL && row + 1 < rows && matrix[row + 1][column] == NOT_BLOCKED && matrix[row + 1][column - 1] == NOT_BLOCKED;
    }

    private boolean moveCounterclockwiseIsPossible(int row, int column, int alignment) {
        return alignment == VERTICAL && column + 1 < columns && matrix[row][column + 1] == NOT_BLOCKED && matrix[row - 1][column + 1] == NOT_BLOCKED;
    }

    private static int change(int alignment) {
        return alignment ^ 1;
    }
}
