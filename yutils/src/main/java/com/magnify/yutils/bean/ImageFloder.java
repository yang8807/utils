package com.magnify.yutils.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageFloder implements Serializable {

    private String dir;

    private String name;

    private int count;
    //目录下面所有的图片
    private ArrayList<String> allImages;
    private String fristImage;

    public ArrayList<String> getAllImages() {
        return allImages;
    }

    public ImageFloder(String dirPath) {
        this.dir = dirPath;
    }

    public ImageFloder(String fristImage, String dirPath) {
        this.dir = dirPath;
        this.fristImage = fristImage;
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

    public String getFristImage() {
        return TextUtils.isEmpty(fristImage) ? "file://" + dir + "/" + allImages.get(0) : fristImage;

    }

    public void setAllImages(ArrayList<String> allImages) {
        this.allImages = allImages;
    }
}
