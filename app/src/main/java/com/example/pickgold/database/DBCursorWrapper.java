package com.example.pickgold.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.pickgold.DayRecord;
import com.example.pickgold.Golds;
import com.example.pickgold.database.DBSchema.DayRecordTable;
import com.example.pickgold.database.DBSchema.GoldsTable;

import java.math.BigDecimal;

public class DBCursorWrapper extends CursorWrapper {
    public DBCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Golds getGolds() {
        String pickDay = getString(getColumnIndex(GoldsTable.GoldsTableCols.PICK_DAY));
        String pickTime = getString(getColumnIndex(GoldsTable.GoldsTableCols.PICK_TIME));
        String owner = getString(getColumnIndex(GoldsTable.GoldsTableCols.OWNER));
        BigDecimal numbers = new BigDecimal(getDouble(getColumnIndex(GoldsTable.GoldsTableCols.NUMBERS)));

        Golds golds = new Golds(numbers,owner);
        golds.setPickDay(pickDay);
        golds.setPickTime(pickTime);

        return golds;
    }

    public DayRecord getDayRecord(){
        String pickDay=getString(getColumnIndex(DayRecordTable.DayRecordTableCols.PICK_DAY));
        BigDecimal countGoldsNumbers=new BigDecimal(getDouble(getColumnIndex(DayRecordTable.DayRecordTableCols.COUNT_GOLDS_NUMBER)));
        int times=getInt(getColumnIndex(DayRecordTable.DayRecordTableCols.TIMES));
        return new DayRecord(pickDay,countGoldsNumbers,times);
    }

}
