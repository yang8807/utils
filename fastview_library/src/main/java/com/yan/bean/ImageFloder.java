package com.yan.bean;

import java.util.List;

public class ImageFloder {

    private String dir;

    private String name;

    private int count;
    //目录下面所有的图片
    private List<String> allImages;

    public List<String> getAllImages() {
        return allImages;
    }

    public ImageFloder(String dirPath) {
        this.dir = dirPath;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public void setAllImages(List<String> allImages) {
        this.allImages = allImages;
    }
}
