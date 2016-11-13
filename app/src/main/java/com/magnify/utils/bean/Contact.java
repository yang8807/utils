package com.magnify.utils.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.datautils.RandomUtil;
import com.example.datautils.User;
import com.magnify.yutils.data.RandomCharUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by heinigger on 16/8/6.
 */
public class Contact implements Parcelable {
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
            peoples.add(RandomUtil.createRandomUser());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupName);
        dest.writeList(this.peoples);
        dest.writeString(this.footerName);
        dest.writeString(this.count);
        dest.writeString(this.sortKey);
    }

    protected Contact(Parcel in) {
        this.groupName = in.readString();
        this.peoples = new ArrayList<User>();
        in.readList(this.peoples, User.class.getClassLoader());
        this.footerName = in.readString();
        this.count = in.readString();
        this.sortKey = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
