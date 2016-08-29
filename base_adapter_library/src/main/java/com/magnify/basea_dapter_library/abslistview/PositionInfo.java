package com.magnify.basea_dapter_library.abslistview;

public class PositionInfo {
    private int count;
    private int startPosition;
    private int parentID;

    public PositionInfo(int count, int startPosition, int parentID) {
        this.count = count;
        this.startPosition = startPosition;
        this.parentID = parentID;
    }

    public int getParentID() {
        return parentID;
    }

    public boolean isRange(int i1) {
        return i1 >= startPosition && i1 < startPosition + count;
    }
}