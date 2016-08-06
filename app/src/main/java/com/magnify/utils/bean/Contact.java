package com.magnify.utils.bean;

import com.example.datautils.RandomUserUtil;
import com.example.datautils.User;
import com.magnify.yutils.RandomCharUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by heinigger on 16/8/6.
 */
public class Contact {
    private String groupName = "";
    private ArrayList<User> peoples;
    private String footerName;
    private String count;
    private static transient Random random = new Random();
    private String sortKey = "";

    public Contact() {
        int nextCount = random.nextInt(50);
        if (peoples == null) peoples = new ArrayList<>();
        for (int i = 0; i < nextCount; i++) {
            peoples.add(RandomUserUtil.createRandomUser());
        }
        this.groupName = RandomCharUtils.getRandomChar(10);
        this.footerName = RandomCharUtils.getRandomChar(20);
        count = String.valueOf(peoples == null ? 0 : peoples.size());
    }

    public Contact(ArrayList<User> users) {
        this.peoples = users;
        if (peoples != null && !peoples.isEmpty())
            this.groupName = this.sortKey = peoples.get(0).getSortKey();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<User> getPeoples() {
        return peoples;
    }

    public void setPeoples(ArrayList<User> peoples) {
        this.peoples = peoples;
    }

    public String getFooterName() {
        return String.format("总共有%s个人", peoples == null ? 0 : peoples.size());
    }

    public void setFooterName(String footerName) {
        this.footerName = footerName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSortKey() {
        return sortKey;
    }
}
