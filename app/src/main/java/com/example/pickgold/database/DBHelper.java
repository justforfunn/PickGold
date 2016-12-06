package com.example.pickgold.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pickgold.database.DBSchema.DayRecordTable;
import com.example.pickgold.database.DBSchema.GoldsTable;


public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "goldsBase.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + GoldsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                GoldsTable.GoldsTableCols.PICK_DAY + ", " +
                GoldsTable.GoldsTableCols.PICK_TIME + ", " +
                GoldsTable.GoldsTableCols.NUMBERS + "," +
                GoldsTable.GoldsTableCols.OWNER + ")"
        );

        db.execSQL("create table " + DayRecordTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                DayRecordTable.DayRecordTableCols.PICK_DAY + ", " +
                DayRecordTable.DayRecordTableCols.COUNT_GOLDS_NUMBER + ", " +
                DayRecordTable.DayRecordTableCols.TIMES + ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //由于增加了列，所以删除原有数据表再重建
        // version 1 -> 2 DBSchema.GoldsTable.NAME表增加了列：金币主，增加了日数据统计表
        if (oldVersion == 1 && newVersion == 2){
            db.execSQL("drop table "+ DATABASE_NAME + "." + GoldsTable.NAME);
            onCreate(db);
        }
    }
}
