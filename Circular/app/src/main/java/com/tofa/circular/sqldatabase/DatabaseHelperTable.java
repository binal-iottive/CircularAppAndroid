package com.tofa.circular.sqldatabase;

public class DatabaseHelperTable {

    public static final String TABLE_NAME_STEPS = "STEPS_TAKEN";
    public static final String COLUMN_STEPS_ID = "id";
    public static final String COLUMN_STEPS_DATE = "date";
    public static final String COLUMN_STEPS_TIME = "time";
    public static final String COLUMN_STEPS_VALUE = "value";

    public static final String TABLE_NAME_WALINKG = "WALINKG_EQUIVALENCY";
    public static final String COLUMN_WALINKG_ID = "id";
    public static final String COLUMN_WALINKG_DATE = "date";
    public static final String COLUMN_WALINKG_TIME = "time";
    public static final String COLUMN_WALINKG_VALUE = "value";

    public static final String TABLE_NAME_CALORIES= "CALORIES_BURNED";
    public static final String COLUMN_CALORIES_ID = "id";
    public static final String COLUMN_CALORIES_DATE = "date";
    public static final String COLUMN_CALORIES_TIME = "time";
    public static final String COLUMN_CALORIES_VALUE = "value";

    public static final String TABLE_NAME_ACTIVE= "ACTIVE_MINUTES";
    public static final String COLUMN_ACTIVE_ID = "id";
    public static final String COLUMN_ACTIVE_DATE = "date";
    public static final String COLUMN_ACTIVE_TIME = "time";
    public static final String COLUMN_ACTIVE_VALUE = "value";

    public static final String TABLE_NAME_VO2= "VO2_MAX";
    public static final String COLUMN_VO2_ID = "id";
    public static final String COLUMN_VO2_DATE = "date";
    public static final String COLUMN_VO2_TIME = "time";
    public static final String COLUMN_VO2_VALUE = "value";

    public static final String TABLE_NAME_HR= "HR_MAX";
    public static final String COLUMN_HR_ID = "id";
    public static final String COLUMN_HR_DATE = "date";
    public static final String COLUMN_HR_TIME = "time";
    public static final String COLUMN_HR_VALUE = "value";

    public static final String TABLE_NAME_HRV= "HRV";
    public static final String TABLE_NAME_ENERGY_LEVEL= "ENERGY_LEVEL";
    public static final String TABLE_NAME_RESTING_HR= "RESTING_HR";
    public static final String TABLE_NAME_SPO2= "SPO2";

    public static final String COLUMN_DEFAULT_TIMESTAMP = "default_timestamp";

    public static final String CREATE_TABLE_STEPS =
            "CREATE TABLE " + TABLE_NAME_STEPS + "("
                    + COLUMN_STEPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_STEPS_DATE + " BLOB,"
                    + COLUMN_STEPS_TIME + " BLOB,"
                    + COLUMN_STEPS_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_WALINKG =
            "CREATE TABLE " + TABLE_NAME_WALINKG + "("
                    + COLUMN_WALINKG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_WALINKG_DATE + " BLOB,"
                    + COLUMN_WALINKG_TIME + " BLOB,"
                    + COLUMN_WALINKG_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_ACTIVE =
            "CREATE TABLE " + TABLE_NAME_ACTIVE + "("
                    + COLUMN_ACTIVE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ACTIVE_DATE + " BLOB,"
                    + COLUMN_ACTIVE_TIME + " BLOB,"
                    + COLUMN_ACTIVE_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_CALORIES =
            "CREATE TABLE " + TABLE_NAME_CALORIES + "("
                    + COLUMN_CALORIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CALORIES_DATE + " BLOB,"
                    + COLUMN_CALORIES_TIME + " BLOB,"
                    + COLUMN_CALORIES_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_VO2 =
            "CREATE TABLE " + TABLE_NAME_VO2 + "("
                    + COLUMN_VO2_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_VO2_DATE + " BLOB,"
                    + COLUMN_VO2_TIME + " BLOB,"
                    + COLUMN_VO2_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_HR =
            "CREATE TABLE " + TABLE_NAME_HR + "("
                    + COLUMN_HR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_HR_DATE + " BLOB,"
                    + COLUMN_HR_TIME + " BLOB,"
                    + COLUMN_HR_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_HRV =
            "CREATE TABLE " + TABLE_NAME_HRV + "("
                    + COLUMN_HR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_HR_DATE + " BLOB,"
                    + COLUMN_HR_TIME + " BLOB,"
                    + COLUMN_HR_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_ENERGY_LEVEL =
            "CREATE TABLE " + TABLE_NAME_ENERGY_LEVEL + "("
                    + COLUMN_HR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_HR_DATE + " BLOB,"
                    + COLUMN_HR_TIME + " BLOB,"
                    + COLUMN_HR_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_RESTING_HR =
            "CREATE TABLE " + TABLE_NAME_RESTING_HR + "("
                    + COLUMN_HR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_HR_DATE + " BLOB,"
                    + COLUMN_HR_TIME + " BLOB,"
                    + COLUMN_HR_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_SPO2 =
            "CREATE TABLE " + TABLE_NAME_SPO2 + "("
                    + COLUMN_HR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_HR_DATE + " BLOB,"
                    + COLUMN_HR_TIME + " BLOB,"
                    + COLUMN_HR_VALUE + " BLOB,"
                    + COLUMN_DEFAULT_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public DatabaseHelperTable() {
    }

    public String dataType;
    public int id;
    public String date;
    public long time;

    public DatabaseHelperTable(String dataType, String date, long time, float value) {
        this.dataType = dataType;
        this.date = date;
        this.time = time;
        this.value = value;
    }

    public float value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
