package com.rok.solver;

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



    public static void main(String[] args) {
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


        new SudokuSolver(field).solve();

    }
}
