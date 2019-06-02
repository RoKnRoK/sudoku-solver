package com.rok.solver;

/**
 * Created by RoK.
 * All rights reserved =)
 */
public class SudokuSolver {

    static final int MAGIC_SUDOKU_NUMBER = 9;

    private final int[][] field;
    private FieldInfo fieldInfo;

    SudokuSolver(int[][] field) {
        this.field = field;
        this.fieldInfo = new FieldInfo(field);
        analyze();
    }


    private void analyze() {
        for (int i = 0; i < MAGIC_SUDOKU_NUMBER; i++) {
            for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                if (field[i][j] != -1) {
                    fieldInfo.addPresent(new Coords(i, j, AreaType.ROW), field[i][j]);
                }
            }
        }
        fieldInfo.print();
    }

    public int[][] solve() {
        do {
            for (int k = 0; k < 3; k++) {
                for (int i = 0; i < MAGIC_SUDOKU_NUMBER; i++) {
                    AreaType areaType = AreaType.fromId(k);
                    for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                        if (fieldInfo.getFromField(new Coords(i, j, areaType)) == -1) {
                            Strategy.NOTHING_EXCEPT_ONE_NUMBER.tryFill(new Coords(i, j, areaType), fieldInfo);
                        }
                    }
                    Strategy.NOWHERE_EXCEPT_ONE_CELL.tryFill(new Coords(i, -1, areaType), fieldInfo);
                }
            }
        } while (!fieldInfo.isFilled());
        return field;
    }

    public int calcBlock(int row, int column) {
        return 3 * (row / 3) + column / 3;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public class FieldInfo {
        private final String ALL_MISSING = "123456789";
        private int totalMissing = 81;

        String[][] missingNumbers = new String[3][MAGIC_SUDOKU_NUMBER];
        String[][] missingIndices = new String[3][MAGIC_SUDOKU_NUMBER];
        String[][] presentNumbers = new String[3][MAGIC_SUDOKU_NUMBER];
        private int[][] field;

        public FieldInfo(int[][] field) {
            this.field = field;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                    missingNumbers[i][j] = ALL_MISSING;
                    presentNumbers[i][j] = "";
                }
            }
        }

        public void addPresent(Coords coords, int number) {
            AreaType areaType = coords.getAreaType();
            int[] intercepting = areaType.getIntercepting();
            addPresentToArea(coords, number);
            addPresentToArea(coords.ofOtherType(AreaType.fromId(intercepting[0])), number);
            addPresentToArea(coords.ofOtherType(AreaType.fromId(intercepting[1])), number);

            int[] normal = coords.toNormal();
            field[normal[0]][normal[1]] = number;
            System.out.println("Row " + normal[0] + ", column " + normal[1] + " found: " + number);
            totalMissing -= 1;
            System.out.println(totalMissing);
        }

        private void addPresentToArea(Coords coords, int number) {
            AreaType areaType = coords.getAreaType();
            int areaIndex = coords.getAreaIndex();
            if (areaType.getAreaId() > 2) {
                throw new IllegalArgumentException("Unknown AreaType " + areaType + " passed!");
            }
            presentNumbers[areaType.getAreaId()][areaIndex] += number;
            missingNumbers[areaType.getAreaId()][areaIndex] =
                    missingNumbers[areaType.getAreaId()][areaIndex].replace(Integer.toString(number), "");
            // missingIndices[areaType.getAreaId()][areaIndex] =
            //          missingIndices[areaType.getAreaId()][areaIndex].replace(Integer.toString(number), "")
        }

        public void print() {
            for (int i = 0; i < 3; i++) {
                System.out.println("Present in " + AreaType.fromId(i) + ": ");
                for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                    System.out.println(presentNumbers[i][j]);
                }
            }

            for (int i = 0; i < 3; i++) {
                System.out.println("Missing in " + AreaType.fromId(i) + ": ");
                for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                    System.out.println(missingNumbers[i][j]);
                }
            }
        }

        public String getMissingInArea(Coords coords) {
            return getMissingOrPresentInArea(coords, missingNumbers);
        }

        public String getPresentInArea(Coords coords) {
            return getMissingOrPresentInArea(coords, presentNumbers);
        }

        private String getMissingOrPresentInArea(Coords coords, String[][] array) {
           /* int areaIndex;
            switch (areaType) {
                case ROW: areaIndex = row; break;
                case COLUMN: areaIndex = column; break;
                case BLOCK: areaIndex = calcBlock(row, column); break;
                default: throw new IllegalArgumentException();
            }*/
            return array[coords.getAreaType().getAreaId()][coords.getAreaIndex()];
        }

        public boolean isFilled() {
            return totalMissing == 0;
        }

        public int[][] getField() {
            return field;
        }

        public int getFromField(Coords coords) {
            int[] normal = coords.toNormal();
            return field[normal[0]][normal[1]];
        }
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
