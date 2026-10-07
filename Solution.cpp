
#include <memory>
#include <vector>
using namespace std;

class Solution {

    struct Point {
        int rowHead{};
        int columnHead{};
        int alignment{};
        int distanceFromStart{};

        Point(int rowHead, int columnHead, int alignment, int distanceFromStart) :
            rowHead{ rowHead }, columnHead{ columnHead },
            alignment{ alignment }, distanceFromStart{ distanceFromStart } {};
    };

    inline static const int VERTICAL = 1;
    inline static const int HORIZONTAL = 0;
    inline static const int NOT_BLOCKED = 0;
    inline static const int NOT_POSSIBLE_TO_REACH_TARGET = -1;

    int rows;
    int columns;
    unique_ptr<vector<vector<int>>> matrix;

public:
    int minimumMoves(vector<vector<int>>& matrix) {
        this->rows = matrix.size();
        this->columns = matrix[0].size();
        this->matrix = make_unique<vector<vector<int>>>(matrix);

        int startRowHead = 0;
        int startColumnHead = 1;
        int targetRowHead = rows - 1;
        int targetColumnHead = columns - 1;

        return findMovesToReachTarget(startRowHead, startColumnHead, targetRowHead, targetColumnHead);
    }

private:
    int findMovesToReachTarget(int startRowHead, int startColumnHead, int targetRowHead, int targetColumnHead) {
        deque<Point> queue;
        queue.emplace_back(startRowHead, startColumnHead, HORIZONTAL, 0);

        vector<vector<vector<bool>>>visited(rows, vector<vector<bool>>(columns, vector<bool>(2)));
        visited[startRowHead][startColumnHead][0] = true;

        while (!queue.empty()) {

            Point current = queue.front();
            queue.pop_front();
            if (current.alignment == HORIZONTAL && current.rowHead == targetRowHead && current.columnHead == targetColumnHead) {
                return current.distanceFromStart;
            }

            int nextDistanceFromStart = current.distanceFromStart + 1;

            bool moveRight = moveRightIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveRight && !visited[current.rowHead][current.columnHead + 1][current.alignment]) {
                visited[current.rowHead][current.columnHead + 1][current.alignment] = true;
                queue.emplace_back(current.rowHead, current.columnHead + 1, current.alignment, nextDistanceFromStart);
            }

            bool moveDown = moveDownIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveDown && !visited[current.rowHead + 1][current.columnHead][current.alignment]) {
                visited[current.rowHead + 1][current.columnHead][current.alignment] = true;
                queue.emplace_back(current.rowHead + 1, current.columnHead, current.alignment, nextDistanceFromStart);
            }

            bool moveClockwise = moveClockwiseIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveClockwise && !visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)]) {
                visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)] = true;
                queue.emplace_back(current.rowHead + 1, current.columnHead - 1, change(current.alignment), nextDistanceFromStart);
            }

            bool moveCounterclockwise = moveCounterclockwiseIsPossible(current.rowHead, current.columnHead, current.alignment);
            if (moveCounterclockwise && !visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)]) {
                visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)] = true;
                queue.emplace_back(current.rowHead - 1, current.columnHead + 1, change(current.alignment), nextDistanceFromStart);
            }
        }

        return NOT_POSSIBLE_TO_REACH_TARGET;
    }

    bool moveRightIsPossible(int row, int column, int alignment) {
        if (alignment == HORIZONTAL && column + 1 < columns && (*matrix)[row][column + 1] == NOT_BLOCKED) {
            return true;
        }
        return (alignment == VERTICAL && column + 1 < columns
                && (*matrix)[row - 1][column + 1] == NOT_BLOCKED
                && (*matrix)[row][column + 1] == NOT_BLOCKED);
    }

    bool moveDownIsPossible(int row, int column, int alignment) {
        if (alignment == VERTICAL && row + 1 < rows && (*matrix)[row + 1][column] == NOT_BLOCKED) {
            return true;
        }
        return alignment == HORIZONTAL && row + 1 < rows
                && (*matrix)[row + 1][column - 1] == NOT_BLOCKED
                && (*matrix)[row + 1][column] == NOT_BLOCKED;
    }

    bool moveClockwiseIsPossible(int row, int column, int alignment) {
        return alignment == HORIZONTAL && row + 1 < rows
                && (*matrix)[row + 1][column] == NOT_BLOCKED
                && (*matrix)[row + 1][column - 1] == NOT_BLOCKED;
    }

    bool moveCounterclockwiseIsPossible(int row, int column, int alignment) {
        return alignment == VERTICAL && column + 1 < columns
                && (*matrix)[row][column + 1] == NOT_BLOCKED
                && (*matrix)[row - 1][column + 1] == NOT_BLOCKED;
    }

    static int change(int alignment) {
        return alignment ^ 1;
    }
};
