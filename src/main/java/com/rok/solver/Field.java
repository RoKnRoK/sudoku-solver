package com.rok.solver;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by RoK.
 * All rights reserved =)
 */
public class Field {

    private static final int MAGIC_SUDOKU_NUMBER = 9;

    private final int[][] field;
    private FieldInfo fieldInfo;

    Field(int[][] field) {
        this.field = field;
        this.fieldInfo = new FieldInfo();
        analyze();
    }


    private void analyze() {
        for (int i = 0; i < MAGIC_SUDOKU_NUMBER; i++) {
            for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                if (field[i][j] != -1) {
                    fieldInfo.addPresent(i, j, field[i][j]);
                }
            }
        }
        //fieldInfo.print();
    }

    public int[][] solve() {
        do {
        for (int i = 0; i < MAGIC_SUDOKU_NUMBER; i++) {
            for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                if (field[i][j] == -1) {
                    tryFill(i, j);
                }
            }
        }
        } while (!fieldInfo.isFilled());
        return field;
    }

    private void tryFill(int row, int column) {
        for (int i= 0; i< 3; i++){
//            AreaType area = AreaType.fromId(i);
            Set<Character> missingInArea = stringToSet(fieldInfo.getMissingInArea(row, column, i));
            int[] intercepting = AreaType.fromId(i).getIntercepting();
            Set<Character> presentInIntercepting1 = stringToSet(fieldInfo.getPresentInArea(row, column, intercepting[0]));
            Set<Character> presentInIntercepting2 = stringToSet(fieldInfo.getPresentInArea(row, column, intercepting[1]));

//            System.out.println(missingInArea);
//            System.out.println(presentInIntercepting1);
//            System.out.println(presentInIntercepting2);
            missingInArea.removeAll(presentInIntercepting1);
            missingInArea.removeAll(presentInIntercepting2);

//            System.out.println(missingInArea);
            if (missingInArea.size() == 1){
                Character found = missingInArea.iterator().next();
                System.out.println("Row " + row + ", column " + column + " found: " + found);
                field[row][column] = found;
                fieldInfo.addPresent(row, column, found);
            }
        }

    }

    private Set<Character> stringToSet(String string) {
        return string.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
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
        String[][] presentNumbers = new String[3][MAGIC_SUDOKU_NUMBER];

        public FieldInfo() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < MAGIC_SUDOKU_NUMBER; j++) {
                    missingNumbers[i][j] = ALL_MISSING;
                    presentNumbers[i][j] = "";
                }
            }
        }
        public void addPresent(int row, int column, int number) {
            addPresent(row, number, AreaType.ROW);
            addPresent(column, number, AreaType.COLUMN);
            addPresent(calcBlock(row, column), number, AreaType.BLOCK);
            totalMissing-=1;
            System.out.println(totalMissing);
        }

        private void addPresent(int areaIndex, int number, AreaType areaType) {
            if (areaType.getAreaId() > 2) {
                throw new IllegalArgumentException("Unknown AreaType " + areaType + " passed!");
            }
            presentNumbers[areaType.getAreaId()][areaIndex] += number;
            missingNumbers[areaType.getAreaId()][areaIndex] =
                    missingNumbers[areaType.getAreaId()][areaIndex].replace(Integer.toString(number), "");
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

        public String getMissingInArea(int row, int column, int areaId) {
            return getMissingOrPresentInArea(row, column, AreaType.fromId(areaId), missingNumbers);
        }

        public String getPresentInArea(int row, int column, int areaId) {
            return getMissingOrPresentInArea(row, column, AreaType.fromId(areaId), presentNumbers);
        }

        private String getMissingOrPresentInArea(int row, int column, AreaType areaType, String[][] array) {
            int areaIndex;
            switch (areaType) {
                case ROW: areaIndex = row; break;
                case COLUMN: areaIndex = column; break;
                case BLOCK: areaIndex = calcBlock(row, column); break;
                default: throw new IllegalArgumentException();
            }
            return array[areaType.getAreaId()][areaIndex];
        }

        public boolean isFilled() {
            return totalMissing == 0;
        }

    }
}
