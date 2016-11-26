package com.example.pickgold;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pickgold.database.GoldsBaseHelper;
import com.example.pickgold.database.GoldsCursorWrapper;
import com.example.pickgold.database.GoldsDbSchema;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GoldsList {

    private static GoldsList sGoldsList;

    private List<Golds> mList;
    private SQLiteDatabase mSQLiteDatabase;

    private GoldsList(Context context){
        mList =new LinkedList<>();
        mSQLiteDatabase=new GoldsBaseHelper(context.getApplicationContext())
                .getWritableDatabase();
    }

    public static GoldsList getInstance(Context context){
        if (sGoldsList ==null){
            sGoldsList=new GoldsList(context);
        }
        return sGoldsList;
    }

    public List<Golds> getGoldsList(){
        mList.clear();
        GoldsCursorWrapper cursor = queryCrimes(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mList.add(cursor.getGolds());
            cursor.moveToNext();
        }
        cursor.close();
        Collections.reverse(mList);
        return mList;
    }

    public void addGolds(Golds golds){
        ContentValues values = getContentValues(golds);
        mSQLiteDatabase.insert(GoldsDbSchema.GoldsTable.NAME, null, values);
    }

    private static ContentValues getContentValues(Golds golds) {
        ContentValues values = new ContentValues();
        values.put(GoldsDbSchema.GoldsTable.Cols.PICK_DAY, golds.getPickDay());
        values.put(GoldsDbSchema.GoldsTable.Cols.PICK_TIME,golds.getPickTime());
        values.put(GoldsDbSchema.GoldsTable.Cols.NUMBERS, golds.getNumber());
        return values;
    }

    private GoldsCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(
                GoldsDbSchema.GoldsTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new GoldsCursorWrapper(cursor);
    }


}
