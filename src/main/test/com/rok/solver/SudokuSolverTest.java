package com.rok.solver;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by RoK.
 * All rights reserved =)
 */
public class SudokuSolverTest {

    @Test
    public void solve() {
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
                {-1, 1, 3, 8, -1, -1, 4, -1, 5},
                {-1, 2, 4, 6, -1, 5, -1, -1, -1},
                {-1, 8, 7, -1, -1, -1, 9, 3, -1},
                {4, 9, -1, 3, -1, 6, -1, -1, -1},
                {-1, -1, 1, -1, -1, -1, 5, -1, -1},
                {-1, -1, -1, 7, -1, 1, -1, 9, 3},
                {-1, 6, 9, -1, -1, -1, 7, 4, -1},
                {-1, -1, -1, 2, -1, 7, 6, 8, -1},
                {1, -1, 2, -1, -1, 8, 3, 5, -1}};;
        int[][] result = new SudokuSolver(field).solve();
        for (int i = 0; i < SudokuSolver.MAGIC_SUDOKU_NUMBER; i++){
            Assert.assertArrayEquals(result[i], solution[i]);
        }

    }

    @Test
    public void calcBlock() {
    }
}
