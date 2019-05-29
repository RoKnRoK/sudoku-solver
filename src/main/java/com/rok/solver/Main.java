package com.rok.solver;

import java.util.Arrays;

public class Main {

    private static final int MAGIC_SUDOKU_NUMBER = 9;

    public static void main(String[] args) {
        int[][] field = {
                {-1,1,3, 8, -1, -1, 4, -1, 5},
                {-1, 2, 4, 6, -1, 5, -1, -1, -1},
                {-1, 8, 7, -1, -1, -1, 9, 3, -1},
                {4, 9, -1, 3, -1, 6, -1, -1, -1},
                {-1, -1, 1, -1, -1, -1, 5, -1, -1},
                {-1, -1, -1, 7, -1, 1, -1, 9, 3},
                {-1, 6, 9, -1, -1, -1, 7, 4, -1},
                {-1, -1, -1, 2, -1, 7, 6, 8, -1},
                {1, -1, 2, -1, -1, 8, 3, 5, -1}};


        solve(field);
    }

    private static void solve(int[][] field) {
        Field field1 = new Field(field);
        field1.calculateRatios();
        field1.printRatios();
    }


    static class Field {
        final int[][] field;
        private double[] rowRatios = new double[MAGIC_SUDOKU_NUMBER];
        private double[] columnRatios  = new double[MAGIC_SUDOKU_NUMBER];
        private double[] blockRatios  = new double[MAGIC_SUDOKU_NUMBER];

        Field(int[][] field) {
            this.field = field;
        }


        public void calculateRatios() {
            for (int i = 0; i< MAGIC_SUDOKU_NUMBER; i++) {
                int notEmptyInRow = 0;
                int notEmptyInColumn = 0;
                for (int j = 0; j< MAGIC_SUDOKU_NUMBER; j++) {
                    if (field[i][j] != -1) {
                        notEmptyInRow++;
                    }
                    if (field[j][i] != -1) {
                        notEmptyInColumn++;
                    }
                }
                rowRatios[i] = 1. * notEmptyInRow/MAGIC_SUDOKU_NUMBER;
                columnRatios[i] = 1. * notEmptyInColumn/MAGIC_SUDOKU_NUMBER;
            }
        }

        public void printRatios() {
            Arrays.stream(rowRatios).forEach(i -> System.out.print(i+ " "));
            System.out.println();
            Arrays.stream(columnRatios).forEach(i -> System.out.print(i+ " "));
        }
    }
}
