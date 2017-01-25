package com.example.pickgold;

import android.support.annotation.Nullable;

public class MyNode {

    private static final String TAG = "MyNode";

    private @Nullable String text;
    private String id;
    private String className;
    private int sleepTime;

    /**
     * 对需要点击的节点的属性进行简单的封装
     * @param text 节点的text属性
     * @param id 节点的id属性
     * @param className 节点的类名
     * @param sleepTime 点击完之后需要休眠时间
     */
    public MyNode(String text, String id,String className,int sleepTime) {
        this.text = text;
        this.id = id;
        this.className=className;
        this.sleepTime=sleepTime;
    }

    public static String getTAG() {
        return TAG;
    }

    @Nullable
    public String getText() {
        return text;
    }

    public void setText(@Nullable String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public String toString() {
        return "MyNode{" +
                "text='" + text + '\'' +
                ", id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", sleepTime=" + sleepTime +
                '}';
    }
}
