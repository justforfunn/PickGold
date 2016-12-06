package com.example.pickgold;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pickgold.database.DBCursorWrapper;
import com.example.pickgold.database.DBHelper;
import com.example.pickgold.database.DBSchema;
import com.example.pickgold.database.DBSchema.DayRecordTable;
import com.example.pickgold.database.DBSchema.GoldsTable;

import java.math.MathContext;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataList {

    private static DataList sInstance;

    private List<Golds> mGoldsList;
    private List<DayRecord> mDayRecordList;
    private SQLiteDatabase mSQLiteDatabase;

    private DataList(Context context){
        mGoldsList =new LinkedList<>();
        mDayRecordList=new LinkedList<>();
        mSQLiteDatabase=new DBHelper(context.getApplicationContext())
                .getWritableDatabase();
    }

    public static DataList getInstance(Context context){
        if (sInstance ==null){
            sInstance =new DataList(context);
        }
        return sInstance;
    }

    public List<Golds> getGoldsList(){
        mGoldsList.clear();
        DBCursorWrapper cursor = queryGolds(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mGoldsList.add(cursor.getGolds());
            cursor.moveToNext();
        }
        cursor.close();
        Collections.reverse(mGoldsList);
        return mGoldsList;
    }

    /**
     * 获取当天的捡金币记录
     * @param pickDay 指定格式的日期
     * @return 金币对象列表
     */
    public List<Golds> getGoldsList(String pickDay){
        mGoldsList.clear();
        DBCursorWrapper cursor = queryGolds(
                GoldsTable.GoldsTableCols.PICK_DAY + " = ?",
                new String[]{ pickDay });

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mGoldsList.add(cursor.getGolds());
            cursor.moveToNext();
        }
        cursor.close();
        Collections.reverse(mGoldsList);
        return mGoldsList;
    }

    public List<DayRecord> getDayRecordList(){
        mDayRecordList.clear();
        DBCursorWrapper cursor = queryDayRecord(null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mDayRecordList.add(cursor.getDayRecord());
            cursor.moveToNext();
        }
        cursor.close();
        Collections.reverse(mDayRecordList);
        return mDayRecordList;
    }


    public void addGolds(Golds golds,boolean flag){
        ContentValues values = getContentValues(golds);
        mSQLiteDatabase.insert(GoldsTable.NAME, null, values);
        countDayRecord(golds,flag);
    }

    /**
     *  根据gold去更新日记录，此处不插入gold记录
     * @param golds 金币
     * @param flag  是否需要更新统计次数
     */
    private void countDayRecord(Golds golds,boolean flag) {
        //进行统计
        DBCursorWrapper cursor=queryDayRecord(DayRecordTable.DayRecordTableCols.PICK_DAY +" = ? ",new String[]{golds.getPickDay()});
        if (cursor.moveToLast()){
            //有记录
            DayRecord dayRecord=cursor.getDayRecord();
            cursor.close();
            if (golds.getPickDay().equals(dayRecord.getPickDay())){
                //记录是今天则更新值
                dayRecord.setCountGoldsNumbers(dayRecord.getCountGoldsNumbers().add(golds.getNumber()));
                if (flag){
                    dayRecord.setTimes(dayRecord.getTimes()+1);
                }
                updateDayRecord(dayRecord);
            }else {
                //否则，增加记录
                int time=0;
                if (flag){
                    time=1;
                }
                addDayRecord(new DayRecord(golds.getPickDay(),golds.getNumber(),time));
            }
        }else {
            //无记录
            int time=0;
            if (flag){
                time=1;
            }
            addDayRecord(new DayRecord(golds.getPickDay(),golds.getNumber(),time));
        }
    }

    private void addDayRecord(DayRecord dayRecord){
        ContentValues values=getContentValues(dayRecord);
        mSQLiteDatabase.insert(DayRecordTable.NAME,null,values);
    }

    private static ContentValues getContentValues(Golds golds) {
        ContentValues values = new ContentValues();
        values.put(GoldsTable.GoldsTableCols.PICK_DAY, golds.getPickDay());
        values.put(GoldsTable.GoldsTableCols.PICK_TIME,golds.getPickTime());
        values.put(GoldsTable.GoldsTableCols.NUMBERS, golds.getNumber().toString());
        values.put(GoldsTable.GoldsTableCols.OWNER, golds.getOwner());
        return values;
    }
    private static ContentValues getContentValues(DayRecord dayRecord) {
        ContentValues values = new ContentValues();
        values.put(DayRecordTable.DayRecordTableCols.PICK_DAY, dayRecord.getPickDay());
        values.put(DayRecordTable.DayRecordTableCols.COUNT_GOLDS_NUMBER,dayRecord.getCountGoldsNumbers().toString());
        values.put(DayRecordTable.DayRecordTableCols.TIMES, dayRecord.getTimes());
        return values;
    }

    private DBCursorWrapper queryGolds(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(
                GoldsTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new DBCursorWrapper(cursor);
    }

    private DBCursorWrapper queryDayRecord(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(
                DayRecordTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new DBCursorWrapper(cursor);
    }

    /**
     * 通过PickDay去找需要更新的记录
     * @param dayRecord DayRecord对象
     * @return 是否更新成功
     */
    private boolean updateDayRecord(DayRecord dayRecord){
        int result=mSQLiteDatabase.update(
                DayRecordTable.NAME,
                getContentValues(dayRecord),
                DayRecordTable.DayRecordTableCols.PICK_DAY + " = ?",
                new String[]{ dayRecord.getPickDay()});
        boolean flag=(result>0);
        return flag;
    }
}
