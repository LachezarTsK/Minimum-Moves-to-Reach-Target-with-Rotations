
using System;
using System.Collections.Generic;

public class Solution
{
    private record Point(int rowHead, int columnHead, int alignment, int distanceFromStart) { }

    private static readonly int VERTICAL = 1;
    private static readonly int HORIZONTAL = 0;
    private static readonly int NOT_BLOCKED = 0;
    private static readonly int NOT_POSSIBLE_TO_REACH_TARGET = -1;

    private int rows;
    private int columns;
    private int[][]? matrix;

    public int MinimumMoves(int[][] matrix)
    {
        rows = matrix.Length;
        columns = matrix[0].Length;
        this.matrix = matrix;

        int startRowHead = 0;
        int startColumnHead = 1;
        int targetRowHead = rows - 1;
        int targetColumnHead = columns - 1;

        return FindMovesToReachTarget(startRowHead, startColumnHead, targetRowHead, targetColumnHead);
    }

    private int FindMovesToReachTarget(int startRowHead, int startColumnHead, int targetRowHead, int targetColumnHead)
    {
        Queue<Point> queue = [];
        queue.Enqueue(new Point(startRowHead, startColumnHead, HORIZONTAL, 0));

        bool[,,] visited = new bool[rows, columns, 2];
        visited[startRowHead, startColumnHead, 0] = true;

        while (queue.Count() > 0)
        {
            Point current = queue.Dequeue();
            if (current.alignment == HORIZONTAL && current.rowHead == targetRowHead && current.columnHead == targetColumnHead)
            {
                return current.distanceFromStart;
            }

            int nextDistanceFromStart = current.distanceFromStart + 1;

            bool moveRight = MoveRightIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveRight && !visited[current.rowHead, current.columnHead + 1, current.alignment])
            {
                visited[current.rowHead, current.columnHead + 1, current.alignment] = true;
                queue.Enqueue(new Point(current.rowHead, current.columnHead + 1, current.alignment, nextDistanceFromStart));
            }

            bool moveDown = MoveDownIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveDown && !visited[current.rowHead + 1, current.columnHead, current.alignment])
            {
                visited[current.rowHead + 1, current.columnHead, current.alignment] = true;
                queue.Enqueue(new Point(current.rowHead + 1, current.columnHead, current.alignment, nextDistanceFromStart));
            }

            bool moveClockwise = MoveClockwiseIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveClockwise && !visited[current.rowHead + 1, current.columnHead - 1, Change(current.alignment)])
            {
                visited[current.rowHead + 1, current.columnHead - 1, Change(current.alignment)] = true;
                queue.Enqueue(new Point(current.rowHead + 1, current.columnHead - 1, Change(current.alignment), nextDistanceFromStart));
            }

            bool moveCounterclockwise = MoveCounterclockwiseIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveCounterclockwise && !visited[current.rowHead - 1, current.columnHead + 1, Change(current.alignment)])
            {
                visited[current.rowHead - 1, current.columnHead + 1, Change(current.alignment)] = true;
                queue.Enqueue(new Point(current.rowHead - 1, current.columnHead + 1, Change(current.alignment), nextDistanceFromStart));
            }
        }

        return NOT_POSSIBLE_TO_REACH_TARGET;
    }

    private bool MoveRightIsPossible(int row, int column, int alignment)
    {
        if (alignment == HORIZONTAL && column + 1 < columns && matrix![row][column + 1] == NOT_BLOCKED)
        {
            return true;
        }
        return (alignment == VERTICAL && column + 1 < columns && matrix![row - 1][column + 1] == NOT_BLOCKED && matrix![row][column + 1] == NOT_BLOCKED);
    }

    private bool MoveDownIsPossible(int row, int column, int alignment)
    {
        if (alignment == VERTICAL && row + 1 < rows && matrix![row + 1][column] == NOT_BLOCKED)
        {
            return true;
        }
        return alignment == HORIZONTAL && row + 1 < rows && matrix![row + 1][column - 1] == NOT_BLOCKED && matrix![row + 1][column] == NOT_BLOCKED;
    }

    private bool MoveClockwiseIsPossible(int row, int column, int alignment)
    {
        return alignment == HORIZONTAL && row + 1 < rows && matrix![row + 1][column] == NOT_BLOCKED && matrix![row + 1][column - 1] == NOT_BLOCKED;
    }

    private bool MoveCounterclockwiseIsPossible(int row, int column, int alignment)
    {
        return alignment == VERTICAL && column + 1 < columns && matrix![row][column + 1] == NOT_BLOCKED && matrix![row - 1][column + 1] == NOT_BLOCKED;
    }

    private static int Change(int alignment)
    {
        return alignment ^ 1;
    }
}
