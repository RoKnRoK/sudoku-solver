package com.rok.solver;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by RoK.
 * All rights reserved =)
 */
public enum Strategy {

    NOTHING_EXCEPT_ONE_NUMBER {
        @Override
        public boolean tryFill(Coords coords, FieldInfo fieldInfo) {
            AreaType areaType = coords.getAreaType();
            Set<Character> missingInArea = stringToSet(fieldInfo.getMissingInArea(coords));

            int[] intercepting = areaType.getIntercepting();
            Set<Character> presentInIntercepting1 = stringToSet(fieldInfo.getPresentInArea(coords.ofOtherType(AreaType.fromId(intercepting[0]))));
            Set<Character> presentInIntercepting2 = stringToSet(fieldInfo.getPresentInArea(coords.ofOtherType(AreaType.fromId(intercepting[1]))));

            missingInArea.removeAll(presentInIntercepting1);
            missingInArea.removeAll(presentInIntercepting2);

            if (missingInArea.size() == 1) {
                Character found = missingInArea.iterator().next();
                fieldInfo.addPresent(coords, Character.getNumericValue(found));
                return true;
            }
            return false;
        }
    },
    NOWHERE_EXCEPT_ONE_CELL {
        @Override
        public boolean tryFill(Coords coords, FieldInfo fieldInfo) {
            AreaType areaType = coords.getAreaType();
            List<Integer> empties = new ArrayList<>();
            List<String> canPut = new ArrayList<>();
            Set<Character> missingInArea = stringToSet(fieldInfo.getMissingInArea(coords));
            for (int j = 0; j < SudokuSolver.MAGIC_SUDOKU_NUMBER; j++) {
                coords.setIndexInArea(j);
                if (fieldInfo.getFromField(coords) == -1) {
                    empties.add(j);
                    int[] intercepting = areaType.getIntercepting();
                    String cannotPut = fieldInfo.getPresentInArea(coords.ofOtherType(AreaType.fromId(intercepting[0]))) +
                            fieldInfo.getPresentInArea(coords.ofOtherType(AreaType.fromId(intercepting[1])));
                    Set<Character> copy = new HashSet<>(missingInArea);
                    copy.removeAll(Strategy.stringToSet(cannotPut));
                    canPut.add(Strategy.setToString(copy));
                }
            }
            String s = String.join(",", canPut);
            String res = s;
            for (int y = 0; y < s.length(); y++) {
                char c1 = s.charAt(y);
                if (c1 == ',') {
                    continue;
                }
                long count = s.chars().filter(c -> c == c1).count();
                if (count > 1) {
                    res = res.replace(Character.toString(c1), "");
                }
            }
//            System.out.println(res);
            String[] split = res.split(",");
            for (int i = 0; i < split.length; i++) {
                String item = split[i];
                if (item.length() == 1) {
                    fieldInfo.addPresent(new Coords(coords.getAreaIndex(), empties.get(i), areaType), Integer.parseInt(split[i]));
                    return true;
                }
            }
            return false;
        }
    },

    RANDOM_PICK {
        @Override
        public boolean tryFill(Coords coords, FieldInfo fieldInfo) {
            AreaType areaType = coords.getAreaType();
            Set<Character> missingInArea = stringToSet(fieldInfo.getMissingInArea(coords));

            int[] intercepting = areaType.getIntercepting();
            Set<Character> presentInIntercepting1 = stringToSet(fieldInfo.getPresentInArea(coords.ofOtherType(AreaType.fromId(intercepting[0]))));
            Set<Character> presentInIntercepting2 = stringToSet(fieldInfo.getPresentInArea(coords.ofOtherType(AreaType.fromId(intercepting[1]))));

            missingInArea.removeAll(presentInIntercepting1);
            missingInArea.removeAll(presentInIntercepting2);

            if (missingInArea.size() == 2) {
                Iterator<Character> iterator = missingInArea.iterator();
                Character firstPossibility = iterator.next();
                Character secondPossibility = iterator.next();
//                System.out.println("Will randomly set " + firstPossibility + " to " + Arrays.toString(coords.toNormal()) + " and see what'll happen");
                fieldInfo.createSnapshot(coords, secondPossibility);
                fieldInfo.addPresent(coords, Character.getNumericValue(firstPossibility));
                return true;
            }
            // trying to fill cell at coords, but no missing cell calculated? that's conflict
            if (missingInArea.size() == 0) {
                fieldInfo.rollback();
                return true;
            }
            return false;
        }
    };




    abstract public boolean tryFill(Coords coords, FieldInfo fieldInfo);

    public static Set<Character> stringToSet(String string) {
        return string.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
    }

    private static String setToString(Set<Character> set) {
        StringBuilder b = new StringBuilder();
        set.forEach(b::append);
        return b.toString();
    }
}
