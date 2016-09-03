package com.magnify.utils.bean;

import java.io.Serializable;

/**
 * Created by ${洒笑天涯} on 2016/9/4.
 */
public class FileType implements Serializable {
    public static final int IMAGE = 1;
    public static final int NORMALFILE = 0;
    private int position;
    private int type;

    public FileType(int position, int type) {
        this.position = position;
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
