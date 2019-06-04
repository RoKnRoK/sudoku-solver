package com.rok.solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by RoK.
 * All rights reserved =)
 */
public class SudokuSolver {

    static final int MAGIC_SUDOKU_NUMBER = 9;

    private FieldInfo fieldInfo;

    SudokuSolver(int[][] field) {
        this.fieldInfo = new FieldInfo(field);
    }


    public int[][] solve() {
        int totalMissingBefore, totalMissingAfter;
        boolean isStuck = false;
        do {
            totalMissingBefore = fieldInfo.getTotalMissing();
            for (int k = 0; k < 3; k++) {
                for (int i = 0; i < MAGIC_SUDOKU_NUMBER; i++) {
//                System.out.println("Checking " + AreaType.fromId(k) + " #" + i);
                    AreaType areaType = AreaType.fromId(k);
                    for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) { // works from j = i as well
                        Coords cellCoords = fieldInfo.getCellCoords(i, j, areaType);
                        if (fieldInfo.getCellValue(cellCoords) == -1) {
                            Strategy strategy = isStuck ? Strategy.RANDOM_PICK : Strategy.NOTHING_EXCEPT_ONE_NUMBER;
                            boolean filled = strategy.tryFill(cellCoords, fieldInfo);
                            if (strategy == Strategy.RANDOM_PICK) {
                                isStuck = !filled;
                            }
                        }
                    }
                    Strategy.NOWHERE_EXCEPT_ONE_CELL.tryFill(fieldInfo.getCellCoords(i, 0, areaType), fieldInfo);
                }
            }
            totalMissingAfter = fieldInfo.getTotalMissing();
            isStuck = totalMissingBefore == totalMissingAfter;
        } while (!fieldInfo.isFilled());
        return fieldInfo.getField();
    }


    public static void main(String[] args) throws FileNotFoundException {
        int[][] field = new int[MAGIC_SUDOKU_NUMBER][MAGIC_SUDOKU_NUMBER];


//        Scanner input = new Scanner(SudokuSolver.class.getResourceAsStream("./sudoku.txt"));
        Scanner input = new Scanner(new File("./sudoku.txt"));

        for (int i = 0; i < MAGIC_SUDOKU_NUMBER; ++i) {
            String row = input.nextLine();
            Scanner rowScanner = new Scanner(row);
            rowScanner.useDelimiter(",");
            for (int j = 0; j < MAGIC_SUDOKU_NUMBER; ++j) {
                if (rowScanner.hasNextInt()) {
                    field[i][j] = rowScanner.nextInt();
                }
            }
        }

        int[][] solved = new SudokuSolver(field).solve();
        Arrays.stream(solved).forEach(row -> {
            Arrays.stream(row).forEach(item -> System.out.print(item + ", "));
            System.out.println();
        });

    }
}
