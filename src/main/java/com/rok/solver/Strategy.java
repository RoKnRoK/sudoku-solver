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
        public int tryFill(Coords coords, SudokuSolver.FieldInfo fieldInfo) {
                AreaType areaType = coords.getAreaType();
                Set<Character> missingInArea = stringToSet(fieldInfo.getMissingInArea(coords));
                int[] intercepting = areaType.getIntercepting();
                Set<Character> presentInIntercepting1 = stringToSet(fieldInfo.getPresentInArea(coords.ofOtherType(AreaType.fromId(intercepting[0]))));
                Set<Character> presentInIntercepting2 = stringToSet(fieldInfo.getPresentInArea(coords.ofOtherType(AreaType.fromId(intercepting[1]))));

//            System.out.println(missingInArea);
//            System.out.println(presentInIntercepting1);
//            System.out.println(presentInIntercepting2);
                missingInArea.removeAll(presentInIntercepting1);
                missingInArea.removeAll(presentInIntercepting2);

            System.out.println(missingInArea);
                if (missingInArea.size() == 1) {
                    Character found = missingInArea.iterator().next();

                    fieldInfo.addPresent(coords, Character.getNumericValue(found));
                    return found;
                }
//            }

            return -1;
        }


    },
    NOWHERE_EXCEPT_ONE_CELL {
        @Override
        public int tryFill(Coords coords, SudokuSolver.FieldInfo fieldInfo) {
            AreaType areaType = coords.getAreaType();
            List<Integer> empties = new ArrayList<>();
                Set<String> canPut = new LinkedHashSet<>();
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
                    if (c1 == ',') {continue;}
                    long count = s.chars().filter(c -> c == c1).count();
                    if (count > 1) {
                        res = res.replace(Character.toString(c1), "");
                    }
                }
            System.out.println(res);
            String[] split = res.split(",");
            for (int i = 0; i < split.length; i++) {
                String item = split[i];
                if (item.length() == 1) {
                    fieldInfo.addPresent(new Coords(coords.getAreaIndex(), empties.get(i), areaType), Integer.parseInt(split[i]));
                }
            }
//            }
            return -1;
        }
    };


    abstract public int tryFill(Coords coords, SudokuSolver.FieldInfo fieldInfo);

    private static Set<Character> stringToSet(String string) {
        return string.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
    }

    private static String setToString(Set<Character> set) {
        StringBuilder b = new StringBuilder();
        set.forEach(b::append);
        return b.toString();
    }
}
