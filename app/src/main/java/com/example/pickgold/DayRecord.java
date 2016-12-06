package com.example.pickgold;

import java.math.BigDecimal;
import java.math.MathContext;

public class DayRecord {

    private String mPickDay;
    private BigDecimal mCountGoldsNumbers;
    private int mTimes;

    public DayRecord(String pickDay, BigDecimal countGoldsNumbers, int times) {
        mPickDay = pickDay;
        mCountGoldsNumbers = countGoldsNumbers.round(MathContext.DECIMAL32);
        mTimes = times;
    }

    public String getPickDay() {
        return mPickDay;
    }

    public void setPickDay(String pickDay) {
        mPickDay = pickDay;
    }

    public BigDecimal getCountGoldsNumbers() {
        return mCountGoldsNumbers;
    }

    public void setCountGoldsNumbers(BigDecimal countGoldsNumbers) {
        mCountGoldsNumbers = countGoldsNumbers.round(MathContext.DECIMAL32);
    }

    public int getTimes() {
        return mTimes;
    }

    public void setTimes(int times) {
        mTimes = times;
    }
}
