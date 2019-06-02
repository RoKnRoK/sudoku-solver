package com.rok.solver;

/**
 * Created by RoK.
 * All rights reserved =)
 */
public class Coords {

    private final int areaIndex;
    private int indexInArea;
    private final AreaType areaType;

    public Coords(int areaIndex, int indexInArea, AreaType areaType) {
        this.areaIndex = areaIndex;
        this.indexInArea = indexInArea;
        this.areaType = areaType;
    }

    public Coords ofOtherType(AreaType type) {
        if (type == this.areaType) {return this;}

        if (type == AreaType.BLOCK) {
            int i = 3*(areaIndex/3) + (indexInArea/3);
            int j = 3*(areaIndex%3) + (indexInArea%3);
            if (this.areaType == AreaType.ROW)
                return new Coords(i, j, type);
            if (this.areaType == AreaType.COLUMN)
                return new Coords(j, i, type);
        }
        if (type == AreaType.ROW) {
            if (this.areaType == AreaType.COLUMN)
                return new Coords(indexInArea, areaIndex, type);
            if (this.areaType == AreaType.BLOCK) {
                int i = 3 * (areaIndex / 3) ;
                int j = indexInArea/3;
                return new Coords(i, j, type);
            }
        }

        if (type == AreaType.COLUMN) {
            if (this.areaType == AreaType.ROW)
                return new Coords(indexInArea, areaIndex, type);
            if (this.areaType == AreaType.BLOCK) {
                int i = indexInArea/3;
                int j = 3 * (areaIndex / 3) ;
                return new Coords(i, j, type);
            }
        }

        throw new IllegalStateException();
    }

    public int[] toNormal() {
        int[] result = new int[2];
        switch (areaType) {
            case ROW: {
                result[0] = areaIndex;
                result[1] = indexInArea;
            }
            break;
            case COLUMN: {
                result[0] = indexInArea;
                result[1] = areaIndex;
            }
            break;
            case BLOCK: {
                result[0] = 3 * (areaIndex / 3) + (indexInArea / 3);
                result[1] = 3 * (areaIndex % 3) + (indexInArea % 3);
            }
            break;
        }
        return result;
    }

    public AreaType getAreaType() {
        return this.areaType;
    }

    public int getAreaIndex() {
        return this.areaIndex;
    }

    public int getIndexInArea() {
        return this.indexInArea;
    }

    public void setIndexInArea(int indexInArea) {
        this.indexInArea = indexInArea;
    }
}
