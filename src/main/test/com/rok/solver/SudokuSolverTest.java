package com.rok.solver;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by RoK.
 * All rights reserved =)
 */
public class SudokuSolverTest {

    @Test
    public void solveEasy() {
        int[][] field = {
                {-1, 1, 3, 8, -1, -1, 4, -1, 5},
                {-1, 2, 4, 6, -1, 5, -1, -1, -1},
                {-1, 8, 7, -1, -1, -1, 9, 3, -1},
                {4, 9, -1, 3, -1, 6, -1, -1, -1},
                {-1, -1, 1, -1, -1, -1, 5, -1, -1},
                {-1, -1, -1, 7, -1, 1, -1, 9, 3},
                {-1, 6, 9, -1, -1, -1, 7, 4, -1},
                {-1, -1, -1, 2, -1, 7, 6, 8, -1},
                {1, -1, 2, -1, -1, 8, 3, 5, -1}};
        int[][] solution = {
                {6, 1, 3, 8, 7, 9, 4, 2, 5},
                {9, 2, 4, 6, 3, 5, 1, 7, 8},
                {5, 8, 7, 1, 2, 4, 9, 3, 6},
                {4, 9, 8, 3, 5, 6, 2, 1, 7},
                {7, 3, 1, 9, 8, 2, 5, 6, 4},
                {2, 5, 6, 7, 4, 1, 8, 9, 3},
                {8, 6, 9, 5, 1, 3, 7, 4, 2},
                {3, 4, 5, 2, 9, 7, 6, 8, 1},
                {1, 7, 2, 4, 6, 8, 3, 5, 9}};
        int[][] result = new SudokuSolver(field).solve();
        for (int i = 0; i < SudokuSolver.MAGIC_SUDOKU_NUMBER; i++) {
            Assert.assertArrayEquals(result[i], solution[i]);
        }

    }

    @Test
    public void solveDifficult() {
        int[][] field = {
                {-1, -1, 2, -1, -1, -1, -1, 4, 1},
                {-1, -1, -1, -1, 8, 2, -1, 7, -1},
                {-1, -1, -1, -1, 4, -1, -1, -1, 9},
                {2, -1, -1, -1, 7, 9, 3, -1, -1},
                {-1, 1, -1, -1, -1, -1, -1, 8, -1},
                {-1, -1, 6, 8, 1, -1, -1, -1, 4},
                {1, -1, -1, -1, 9, -1, -1, -1, -1},
                {-1, 6, -1, 4, 3, -1, -1, -1, -1},
                {8, 5, -1, -1, -1, -1, 4, -1, -1}};
        int[][] solution = {
                {6, 1, 3, 8, 7, 9, 4, 2, 5},
                {9, 2, 4, 6, 3, 5, 1, 7, 8},
                {5, 8, 7, 1, 2, 4, 9, 3, 6},
                {4, 9, 8, 3, 5, 6, 2, 1, 7},
                {7, 3, 1, 9, 8, 2, 5, 6, 4},
                {2, 5, 6, 7, 4, 1, 8, 9, 3},
                {8, 6, 9, 5, 1, 3, 7, 4, 2},
                {3, 4, 5, 2, 9, 7, 6, 8, 1},
                {1, 7, 2, 4, 6, 8, 3, 5, 9}};
        int[][] result = new SudokuSolver(field).solve();
        for (int i = 0; i < SudokuSolver.MAGIC_SUDOKU_NUMBER; i++) {
            Assert.assertArrayEquals(result[i], solution[i]);
        }

    }
}
