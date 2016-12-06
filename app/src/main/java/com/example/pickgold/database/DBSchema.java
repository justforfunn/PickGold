package com.example.pickgold.database;

public class DBSchema {
    public static final class GoldsTable {
        public static final String NAME = "golds";

        public static final class GoldsTableCols {
            public static final String PICK_DAY = "pick_day";
            public static final String PICK_TIME = "pick_time";
            public static final String NUMBERS = "numbers";
            public static final String OWNER = "owner";
        }
    }
    public static final class DayRecordTable{
        public static final String NAME = "day_record";

        public static final class DayRecordTableCols {
            public static final String PICK_DAY = "pick_day";
            public static final String COUNT_GOLDS_NUMBER = "count_numbers";
            public static final String TIMES = "times";
        }
    }
}
