package com.example.pickgold;

import android.support.annotation.Nullable;

public class MyNode {

    private static final String TAG = "MyNode";

    private @Nullable String text;
    private String id;
    private int backToParent;
    private int childPosition;
    private String className;

    /**
     * 对需要点击的节点的属性进行简单的封装
     * @param text  节点的text属性
     * @param id    节点的id属性
     * @param backToParent  返回第几层的parent
     * @param childPosition 需要点击的子节点的位置
     */
    public MyNode(String text, String id,String className, int backToParent, int childPosition) {
        this.text = text;
        this.id = id;
        this.className=className;
        this.backToParent = backToParent;
        this.childPosition = childPosition;
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

    public int getBackToParent() {
        return backToParent;
    }

    public void setBackToParent(int backToParent) {
        this.backToParent = backToParent;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "MyNode{" +
                "text='" + text + '\'' +
                ", id='" + id + '\'' +
                ", backToParent=" + backToParent +
                ", childPosition=" + childPosition +
                '}';
    }
}
