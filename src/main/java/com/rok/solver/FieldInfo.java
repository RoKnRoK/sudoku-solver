package com.rok.solver;

import static com.rok.solver.SudokuSolver.MAGIC_SUDOKU_NUMBER;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

class FieldInfo {
    private final String ALL_MISSING = "123456789";
    private int totalMissing = 81;

    private String[][] missingNumbers = new String[3][MAGIC_SUDOKU_NUMBER];
    private String[][] presentNumbers = new String[3][MAGIC_SUDOKU_NUMBER];
    private int[][] field;
    private final Coords[][] coords = new Coords[MAGIC_SUDOKU_NUMBER][MAGIC_SUDOKU_NUMBER];

    private Deque<FieldInfo> snapshots = new LinkedList<>();

    FieldInfo(int[][] field) {
        this.field = Arrays.stream(field).map(int[]::clone).toArray(int[][]::new);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                missingNumbers[i][j] = ALL_MISSING;
                presentNumbers[i][j] = "";
            }
        }
        init();
    }

    private void init() {
        for (int i = 0; i < MAGIC_SUDOKU_NUMBER; i++) {
            for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                coords[i][j] = new Coords(i, j, AreaType.ROW).withRepresentation();
                if (field[i][j] != -1) {
                    this.addPresent(coords[i][j], field[i][j]);
                }
            }
        }
    }

    void addPresent(Coords coords, int number) {
        AreaType areaType = coords.getAreaType();
        int[] intercepting = areaType.getIntercepting();
        addPresentToArea(coords, number);
        addPresentToArea(coords.toType(AreaType.fromId(intercepting[0])), number);
        addPresentToArea(coords.toType(AreaType.fromId(intercepting[1])), number);

        int[] normal = coords.asNormal();
        field[normal[0]][normal[1]] = number;
//        System.out.println("Row " + (normal[0] + 1) + ", column " + (normal[1] + 1) + " set to " + number);
        totalMissing -= 1;
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
    }

    String getMissingInArea(Coords coords) {
        return getMissingOrPresentInArea(coords, missingNumbers);
    }

    String getPresentInArea(Coords coords) {
        return getMissingOrPresentInArea(coords, presentNumbers);
    }

    private String getMissingOrPresentInArea(Coords coords, String[][] array) {
        return array[coords.getAreaType().getAreaId()][coords.getAreaIndex()];
    }

    boolean isFilled() {
        return totalMissing == 0;
    }

    int[][] getField() {
        return field;
    }

    int getCellValue(Coords coords) {
        int[] normal = coords.asNormal();
        return field[normal[0]][normal[1]];
    }

    Coords getCellCoords(int areaIndex, int indexInArea, AreaType areaType) {
        int i, j;
        switch (areaType) {
            case ROW: {
                i = areaIndex;
                j = indexInArea;
            }
            break;
            case COLUMN: {
                i = indexInArea;
                j = areaIndex;
            }
            break;
            case BLOCK: {
                i = 3 * (areaIndex / 3) + (indexInArea / 3);
                j = 3 * (areaIndex % 3) + (indexInArea % 3);
            }
            break;
            default: throw new IllegalStateException();
        }

        return coords[i][j].toType(areaType);
    }

    int getTotalMissing() {
        return this.totalMissing;
    }

    void createSnapshot(Coords coords, Character number) {
        FieldInfo copy = this.copy();
        copy.addPresent(coords, Character.getNumericValue(number));
        snapshots.addFirst(copy);
    }

    private FieldInfo copy() {
        FieldInfo copy = new FieldInfo(this.field);
        copy.missingNumbers = Arrays.stream(this.missingNumbers).map(String[]::clone).toArray(String[][]::new);
        copy.presentNumbers = Arrays.stream(this.presentNumbers).map(String[]::clone).toArray(String[][]::new);
        copy.totalMissing = this.totalMissing;
        return copy;
    }

    void rollback() {
        FieldInfo snapshot = snapshots.removeFirst();
        this.field = Arrays.stream(snapshot.field).map(int[]::clone).toArray(int[][]::new);
        this.missingNumbers = Arrays.stream(snapshot.missingNumbers).map(String[]::clone).toArray(String[][]::new);
        this.presentNumbers = Arrays.stream(snapshot.presentNumbers).map(String[]::clone).toArray(String[][]::new);
        this.totalMissing = snapshot.getTotalMissing();
    }
}
