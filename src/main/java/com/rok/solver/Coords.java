package com.rok.solver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by RoK.
 * All rights reserved =)
 */
class Coords {

    private final int areaIndex;
    private int indexInArea;
    private final AreaType areaType;

    private final Map<AreaType, Coords> representation = new HashMap<>();

    private int[] normal;

    Coords(int areaIndex, int indexInArea, AreaType areaType) {
        this.areaIndex = areaIndex;
        this.indexInArea = indexInArea;
        this.areaType = areaType;
    }

    Coords withRepresentation() {
        Set<AreaType> areaTypes = new HashSet<>(Arrays.asList(AreaType.values()));
        areaTypes.remove(this.areaType);
        areaTypes.forEach(type -> {
            representation.put(type, ofOtherType(type));
        });
        representation.put(areaType, this);
        representation.values().forEach(item -> {
            item.representation.putAll(representation);
            item.normal = new int[]{representation.get(AreaType.ROW).getAreaIndex(), representation.get(AreaType.ROW).getIndexInArea()};
        });
        normal = new int[]{representation.get(AreaType.ROW).getAreaIndex(), representation.get(AreaType.ROW).getIndexInArea()};
        return this;
    }

    Coords toType(AreaType type) {
        return representation.get(type);
    };

    private Coords ofOtherType(AreaType type) {
        if (type == this.areaType) {
            return this;
        }

        if (type == AreaType.BLOCK) {
            if (this.areaType == AreaType.ROW) {
                int i = 3 * (areaIndex / 3) + (indexInArea / 3);
                int j = 3 * (areaIndex % 3) + (indexInArea % 3);
                return new Coords(i, j, type);
            }
            if (this.areaType == AreaType.COLUMN) {
                int i = 3 * (indexInArea / 3) + (areaIndex / 3);
                int j = 3 * (indexInArea % 3) + (areaIndex % 3);
                return new Coords(i, j, type);
            }
        }
        if (type == AreaType.ROW) {
            if (this.areaType == AreaType.COLUMN)
                return new Coords(indexInArea, areaIndex, type);
            if (this.areaType == AreaType.BLOCK) {
                int i = 3 * (areaIndex / 3) + (indexInArea / 3);
                int j = 3 * (areaIndex % 3) + (indexInArea % 3);
                return new Coords(i, j, type);
            }
        }

        if (type == AreaType.COLUMN) {
            if (this.areaType == AreaType.ROW)
                return new Coords(indexInArea, areaIndex, type);
            if (this.areaType == AreaType.BLOCK) {
                int i = 3 * (areaIndex % 3) + (indexInArea % 3);
                int j = 3 * (areaIndex / 3) + (indexInArea / 3);
                return new Coords(i, j, type);
            }
        }

        throw new IllegalStateException();
    }

    int[] asNormal() {
        return normal;
    }

    AreaType getAreaType() {
        return this.areaType;
    }

    int getAreaIndex() {
        return this.areaIndex;
    }

    private int getIndexInArea() {
        return indexInArea;
    }

}
