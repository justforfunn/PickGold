package com.example.pickgold.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class GoldsBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "goldsBase.db";

    public GoldsBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + GoldsDbSchema.GoldsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                GoldsDbSchema.GoldsTable.Cols.PICK_DAY + ", " +
                GoldsDbSchema.GoldsTable.Cols.PICK_TIME + ", " +
                GoldsDbSchema.GoldsTable.Cols.NUMBERS +  ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //暂时不需要升级数据库
    }
}
