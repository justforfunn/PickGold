package com.example.pickgold;

import java.util.Calendar;
import java.util.Date;

public class Golds {

    private String mPickDay;
    private String mPickTime;
    private double mNumber;

    public Golds(double number) {
        mNumber = number;

        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        mPickDay=year+"年"+month+"月"+day+"日";
        if (minute>=0&&minute<=9){
            mPickTime=hour+":0"+minute;
        }else {
            mPickTime=hour+":"+minute;
        }
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

    public double getNumber() {
        return mNumber;
    }

    public void setNumber(double number) {
        mNumber = number;
    }
}
