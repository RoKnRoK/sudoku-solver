package com.rok.solver;

import static com.rok.solver.SudokuSolver.MAGIC_SUDOKU_NUMBER;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class FieldInfo {
    private final String ALL_MISSING = "123456789";
    private int totalMissing = 81;

    private String[][] missingNumbers = new String[3][MAGIC_SUDOKU_NUMBER];
    private String[][] presentNumbers = new String[3][MAGIC_SUDOKU_NUMBER];
    private int[][] field;

    private Deque<FieldInfo> snapshots = new LinkedList<>();

    public FieldInfo(int[][] field) {
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
                if (field[i][j] != -1) {
                    this.addPresent(new Coords(i, j, AreaType.ROW), field[i][j]);
                }
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
        System.out.println("Row " + (normal[0] + 1) + ", column " + (normal[1] + 1) + " found: " + number);
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
        //  System.out.println("Present " + presentNumbers[areaType.getAreaId()][areaIndex]);
        missingNumbers[areaType.getAreaId()][areaIndex] =
                missingNumbers[areaType.getAreaId()][areaIndex].replace(Integer.toString(number), "");
        //   System.out.println("Missing " + missingNumbers[areaType.getAreaId()][areaIndex]);
    }
/*
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
        }*/

    public String getMissingInArea(Coords coords) {
        return getMissingOrPresentInArea(coords, missingNumbers);
    }

    public String getPresentInArea(Coords coords) {
        return getMissingOrPresentInArea(coords, presentNumbers);
    }

    private String getMissingOrPresentInArea(Coords coords, String[][] array) {
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

    public int getTotalMissing() {
        return this.totalMissing;
    }

    public void createSnapshot(Coords coords, Character number) {
        FieldInfo copy = this.copy();
        copy.addPresent(coords, Character.getNumericValue(number));
        snapshots.addFirst(copy);
        // System.out.println("Snapshot created");
    }

    private FieldInfo copy() {
        FieldInfo copy = new FieldInfo(this.field);
        copy.missingNumbers = Arrays.stream(this.missingNumbers).map(String[]::clone).toArray(String[][]::new);
        copy.presentNumbers = Arrays.stream(this.presentNumbers).map(String[]::clone).toArray(String[][]::new);
        copy.totalMissing = this.totalMissing;
        return copy;
    }

    public boolean hasConflicts(Coords coords) {
        System.out.println("Check conflicts");
        int[] normal = coords.toNormal();
        int blockIndex = coords.ofOtherType(AreaType.BLOCK).getAreaIndex();
        int rowIndex = normal[0];
        int columnIndex = normal[1];

        return hasDuplicates(presentNumbers[0][rowIndex]) &&
                hasDuplicates(presentNumbers[1][columnIndex]) &&
                hasDuplicates(presentNumbers[2][blockIndex]);
    }

    private boolean hasDuplicates(String s) {
        return Strategy.stringToSet(s).size() < s.length();
    }

    public void rollback() {
        System.out.println("Rollback...");
        FieldInfo snapshot = snapshots.removeFirst();
        this.field = Arrays.stream(snapshot.field).map(int[]::clone).toArray(int[][]::new);
        this.missingNumbers = Arrays.stream(snapshot.missingNumbers).map(String[]::clone).toArray(String[][]::new);
        this.presentNumbers = Arrays.stream(snapshot.presentNumbers).map(String[]::clone).toArray(String[][]::new);
        this.totalMissing = snapshot.getTotalMissing();
    }
}
