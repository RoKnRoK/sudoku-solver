package com.rok.solver;

/**
 * Created by RoK.
 * All rights reserved =)
 */

public enum AreaType {
    ROW(0), COLUMN(1), BLOCK(2);

    private final int areaId;

    AreaType(int i) {
        this.areaId = i;
    }

    public int getAreaId() {
        return areaId;
    }

    public static AreaType fromId(int i){
        for (AreaType type : values()){
            if (type.getAreaId() == i) {
                return type;
            }
        }
        throw new IllegalArgumentException("No Areatype with id " + i);
    }

    public int[] getIntercepting() {
        if (areaId == 0) return new int[]{1, 2};
        else if (areaId == 1) return new int[]{0, 2};
        else return new int[]{0, 1};
    }}
