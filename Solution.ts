
// const {Queue} = require('@datastructures-js/queue');
/*
 Queue is internally included in the solution file on leetcode.
 When running the code on leetcode it should stay commented out. 
 It is mentioned here just for information about the external library 
 that is applied for this data structure.
 */

/**
   * @param {number[][]} matrix
   * @return {number}
   */
function minimumMoves(matrix: number[][]): number {
    const util = new Util(matrix);
    const startRowHead = 0;
    const startColumnHead = 1;
    const targetRowHead = util.rows - 1;
    const targetColumnHead = util.columns - 1;

    return findMovesToReachTarget(startRowHead, startColumnHead, targetRowHead, targetColumnHead, util);
};

/**
 * @param {number}startRowHead
 * @param {number}startColumnHead
 * @param {number}targetRowHead
 * @param {number} targetColumnHead 
 * @param {Util}util
 * @return {number} 
 */
function findMovesToReachTarget(startRowHead, startColumnHead, targetRowHead, targetColumnHead, util) {
    //  Queue<Point>   
    const queue = new Queue<Point>();
    queue.enqueue(new Point(startRowHead, startColumnHead, Util.HORIZONTAL, 0));

    const visited: boolean[][][] = Array.from(new Array(util.rows), () => Array.from(new Array(util.columns), () => new Array(2).fill(false)));
    visited[startRowHead][startColumnHead][0] = true;

    while (!queue.isEmpty()) {

        const current = queue.dequeue();
        if (current.alignment === Util.HORIZONTAL && current.rowHead === targetRowHead && current.columnHead === targetColumnHead) {
            return current.distanceFromStart;
        }

        const nextDistanceFromStart = current.distanceFromStart + 1;

        const moveRight = moveRightIsPossible(current.rowHead, current.columnHead, current.alignment, util);
        if (moveRight && !visited[current.rowHead][current.columnHead + 1][current.alignment]) {
            visited[current.rowHead][current.columnHead + 1][current.alignment] = true;
            queue.enqueue(new Point(current.rowHead, current.columnHead + 1, current.alignment, nextDistanceFromStart));
        }

        const moveDown = moveDownIsPossible(current.rowHead, current.columnHead, current.alignment, util);
        if (moveDown && !visited[current.rowHead + 1][current.columnHead][current.alignment]) {
            visited[current.rowHead + 1][current.columnHead][current.alignment] = true;
            queue.enqueue(new Point(current.rowHead + 1, current.columnHead, current.alignment, nextDistanceFromStart));
        }

        const moveClockwise = moveClockwiseIsPossible(current.rowHead, current.columnHead, current.alignment, util);
        if (moveClockwise && !visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)]) {
            visited[current.rowHead + 1][current.columnHead - 1][change(current.alignment)] = true;
            queue.enqueue(new Point(current.rowHead + 1, current.columnHead - 1, change(current.alignment), nextDistanceFromStart));
        }

        const moveCounterclockwise = moveCounterclockwiseIsPossible(current.rowHead, current.columnHead, current.alignment, util);
        if (moveCounterclockwise && !visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)]) {
            visited[current.rowHead - 1][current.columnHead + 1][change(current.alignment)] = true;
            queue.enqueue(new Point(current.rowHead - 1, current.columnHead + 1, change(current.alignment), nextDistanceFromStart));
        }
    }

    return Util.NOT_POSSIBLE_TO_REACH_TARGET;
}

function moveRightIsPossible(row: number, column: number, alignment: number, util: Util): boolean {
    if (alignment === Util.HORIZONTAL && column + 1 < util.columns && util.matrix[row][column + 1] === Util.NOT_BLOCKED) {
        return true;
    }
    return (alignment === Util.VERTICAL && column + 1 < util.columns && util.matrix[row - 1][column + 1] === Util.NOT_BLOCKED && util.matrix[row][column + 1] === Util.NOT_BLOCKED);
}

function moveDownIsPossible(row: number, column: number, alignment: number, util: Util): boolean {
    if (alignment === Util.VERTICAL && row + 1 < util.rows && util.matrix[row + 1][column] === Util.NOT_BLOCKED) {
        return true;
    }
    return alignment === Util.HORIZONTAL && row + 1 < util.rows && util.matrix[row + 1][column - 1] === Util.NOT_BLOCKED && util.matrix[row + 1][column] === Util.NOT_BLOCKED;
}

function moveClockwiseIsPossible(row: number, column: number, alignment: number, util: Util): boolean {
    return alignment === Util.HORIZONTAL && row + 1 < util.rows && util.matrix[row + 1][column] === Util.NOT_BLOCKED && util.matrix[row + 1][column - 1] === Util.NOT_BLOCKED;
}

function moveCounterclockwiseIsPossible(row: number, column: number, alignment: number, util: Util): boolean {
    return alignment === Util.VERTICAL && column + 1 < util.columns && util.matrix[row][column + 1] === Util.NOT_BLOCKED && util.matrix[row - 1][column + 1] === Util.NOT_BLOCKED;
}

function change(alignment: number): number {
    return alignment ^ 1;
}

class Point {
    constructor(public rowHead: number, public columnHead: number, public alignment: number, public distanceFromStart: number) {
    }
}

class Util {

    static VERTICAL = 1;
    static HORIZONTAL = 0;
    static NOT_BLOCKED = 0;
    static NOT_POSSIBLE_TO_REACH_TARGET = -1;

    rows: number;
    columns: number;
    matrix: number[][];

    constructor(matrix: number[][]) {
        this.rows = matrix.length;
        this.columns = matrix[0].length;
        this.matrix = matrix;
    }
}
