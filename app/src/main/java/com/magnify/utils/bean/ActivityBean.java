package com.magnify.utils.bean;

/**
 * Created by heinigger on 16/8/2.
 */
public class ActivityBean {
    /*工具的名字或者控件名字*/
    private String name;
    /*功能描述*/
    private String description;
    /*跳转的页面*/
    private Class aClass;

    private Object[] object;

    public ActivityBean(String text, String description, Class<?> clazz, Object[] objects) {
        this.name = text;
        this.description = description;
        this.aClass = clazz;
        object = objects;
    }

    public Object[] getObject() {
        return object;
    }


    public ActivityBean(String name, String description, Class aClass) {
        this.name = name;
        this.description = description;
        this.aClass = aClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
