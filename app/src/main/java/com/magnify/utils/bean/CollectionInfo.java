package com.magnify.utils.bean;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by heinigger on 16/8/4.
 */
public class CollectionInfo {
    /*组名*/
    private String groupName;
    /*关于该分类的描述*/
    private String description;
    private ArrayList<Child> children;

    public CollectionInfo(String groupName, String description) {
        this.description = description;
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

    public CollectionInfo addChild(Child... childs) {
        if (children == null) children = new ArrayList<>();
        children.addAll(Arrays.asList(childs));
        return this;
    }

    public CollectionInfo addChild(String github_url) {
        if (children == null) children = new ArrayList<>();
        children.add(new Child(github_url));
        return this;
    }

    public int getChildSize() {
        return children == null ? 0 : children.size();
    }

    public CollectionInfo addChild(String github_url, String usage) {
        if (children == null) children = new ArrayList<>();
        children.add(new Child(github_url, usage));
        return this;
    }

    public static class Child {
        /*子item的名字*/
        private String childName="";
        /*github的地址*/
        private String git_hub_url="";
        /*关于该控件的使用*/
        private String description="";
        /*使用方法*/
        private String usage="";

        public String getUsage() {
            return usage;
        }

        public void setUsage(String usage) {
            this.usage = usage;
        }

        public Child(String github_url) {
            this.git_hub_url = github_url;
        }

        public Child(String github_url, String usage) {
            this.git_hub_url = github_url;
            this.usage = usage;
        }

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public String getGit_hub_url() {
            return git_hub_url;
        }

        public void setGit_hub_url(String git_hub_url) {
            this.git_hub_url = git_hub_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
