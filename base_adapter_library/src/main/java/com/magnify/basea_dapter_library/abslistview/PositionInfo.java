package com.magnify.basea_dapter_library.abslistview;

public class PositionInfo {
    private int count;
    private int startPosition;

    public PositionInfo(int count, int startPosition) {
        this.count = count;
        this.startPosition = startPosition;
    }

    public boolean isRange(int i1) {
        return i1 >= startPosition && i1 < startPosition + count;
    }
}