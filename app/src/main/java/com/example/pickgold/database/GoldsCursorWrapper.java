package com.example.pickgold.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.pickgold.Golds;

public class GoldsCursorWrapper extends CursorWrapper {
    public GoldsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Golds getGolds() {
        String pickDay = getString(getColumnIndex(GoldsDbSchema.GoldsTable.Cols.PICK_DAY));
        String pickTime = getString(getColumnIndex(GoldsDbSchema.GoldsTable.Cols.PICK_TIME));
        double numbers = getDouble(getColumnIndex(GoldsDbSchema.GoldsTable.Cols.NUMBERS));

        Golds golds = new Golds(numbers);
        golds.setPickDay(pickDay);
        golds.setPickTime(pickTime);

        return golds;
    }
}
