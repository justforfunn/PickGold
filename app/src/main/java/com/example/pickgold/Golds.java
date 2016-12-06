package com.example.pickgold;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Golds {

    private String mPickDay;
    private String mPickTime;
    private String mOwner;
    private BigDecimal mNumber;

    /**
     * 用当前的时间实例化对象
     * @param number 金币数
     * @param owner 金币主
     */
    public Golds(BigDecimal number,String owner) {
        mNumber = number.round(MathContext.DECIMAL32);
        String[] dateStringArr=dateToStringArr(new Date());
        mPickDay=dateStringArr[0];
        mPickTime=dateStringArr[1];
        mOwner=owner;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public static String[] dateToStringArr(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy.MM.dd,HH:mm:ss");
        String dateString=simpleDateFormat.format(date);
        return dateString.split(",");
    }

    public String getPickDay() {
        return mPickDay;
    }

    public void setPickDay(String pickDay) {
        mPickDay = pickDay;
    }

    public String getPickTime() {
        return mPickTime;
    }

    public void setPickTime(String pickTime) {
        mPickTime = pickTime;
    }

    public BigDecimal getNumber() {
        return mNumber;
    }

    public void setNumber(BigDecimal number) {
        mNumber = number.round(MathContext.DECIMAL32);
    }
}
