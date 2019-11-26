package com.tofa.circular.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CIRCULAR_DATABASE";

    public DatabaseHelper(Context context) {
      /*  super(context, Environment.getExternalStorageDirectory()
                + File.separator + "CIRCULAR"
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);*/
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseHelperTable.CREATE_TABLE_STEPS);
        db.execSQL(DatabaseHelperTable.CREATE_TABLE_WALINKG);
        db.execSQL(DatabaseHelperTable.CREATE_TABLE_CALORIES);
        db.execSQL(DatabaseHelperTable.CREATE_TABLE_ACTIVE);
        db.execSQL(DatabaseHelperTable.CREATE_TABLE_VO2);
        db.execSQL(DatabaseHelperTable.CREATE_TABLE_HR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelperTable.TABLE_NAME_STEPS);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelperTable.TABLE_NAME_WALINKG);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelperTable.TABLE_NAME_CALORIES);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelperTable.TABLE_NAME_ACTIVE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelperTable.TABLE_NAME_VO2);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelperTable.TABLE_NAME_HR);
        onCreate(db);
    }

    public long insertStepsTakenData(String tableName, String Date, long Time, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_STEPS_DATE, Date);
        values.put(DatabaseHelperTable.COLUMN_STEPS_TIME, Time);
        values.put(DatabaseHelperTable.COLUMN_STEPS_VALUE, value);
        long id = db.insert(tableName, null, values);
        db.close();
        return id;
    }

    public void delete(int rowId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String table = DatabaseHelperTable.TABLE_NAME_STEPS;
            String whereClause = "_id=?";
            String[] whereArgs = new String[]{String.valueOf(rowId)};
            db.delete(table, whereClause, whereArgs);
            db.close();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }

    public long deleteRow(int rowId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(rowId)};

        long id = db.delete(DatabaseHelperTable.TABLE_NAME_STEPS, whereClause, whereArgs);
        db.close();
        return id;
    }

    public int getLastRecordId() {
        /*String selectQuery = "SELECT id FROM "
                + DatabaseHelperTable.TABLE_NAME_STEPS
                + " ORDER BY '"
                +  DatabaseHelperTable.COLUMN_STEPS_ID
                +"' DESC LIMIT 1";*/
        String selectQuery = "select max(id) from " + DatabaseHelperTable.TABLE_NAME_STEPS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

//        int ID = 0;
        try {
            cursor.moveToFirst();
            String sum = cursor.getString(0);

            if (sum == null)
                return 1;
            return (Integer.parseInt(sum));
        } finally {
            cursor.close();
        }
        /*if (cursor.moveToFirst()) {
            do {
                ID = cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_ID));
            } while (cursor.moveToNext());
        }
        db.close();*/
//        return ID;
    }


   /*
    public int getSumTotalSteps(String todatDate) {
        //select sum(total_steps) from LIVE_WORKOUT where workout_start_date = "09/03/2019";
        String selectQuery = "select sum(" + DatabaseHelperTable.COLUMN_WORKOUT_STEPS + ") from "
                + DatabaseHelperTable.TABLE_NAME_STEPS + " WHERE "
                +  DatabaseHelperTable.COLUMN_STEPS_VALUE
                + " = '"
                + todatDate
                + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            cursor.moveToFirst();
            String sum = cursor.getString(0);

            if (sum == null)
                return 1;
            return (Integer.parseInt(sum));
        } finally {
            cursor.close();
        }
    }

    public double getSumTotalCalories(String todatDate) {
        //select sum(total_steps) from LIVE_WORKOUT where workout_start_date = "09/03/2019";
        String selectQuery = "select sum(" + DatabaseHelperTable.COLUMN_WORKOUT_CALORY + ") from "
                + DatabaseHelperTable.TABLE_NAME_STEPS + " WHERE "
                +  DatabaseHelperTable.COLUMN_STEPS_VALUE
                + " = '"
                + todatDate
                + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            cursor.moveToFirst();
            String sum = cursor.getString(0);

            if (sum == null)
                return 1;
            return (Double.parseDouble(sum));
        } finally {
            cursor.close();
        }
    }

    public long updateDailyAverageData(int getId,double dailyAvgHr,double dailyAvgBr,long dailyTotalSteps, double dailyTotalCalories,  double dailyAvgPace) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelperTable.COLUMN_DAILY_AVG_HR, dailyAvgHr);
        values.put(DatabaseHelperTable.COLUMN_DAILY_AVG_BR, dailyAvgBr);
        values.put(DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS, dailyTotalSteps);
        values.put(DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES, dailyTotalCalories);
        values.put(DatabaseHelperTable.COLUMN_DAILY_HVR, dailyAvgPace);

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updatePreviousDateAverageData(long getId,double calories,double dailyAvgHr,double dailyAvgBr,long dailyTotalSteps, double dailyTotalCalories,  double dailyAvgPace) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelperTable.COLUMN_WORKOUT_CALORY, calories);
        values.put(DatabaseHelperTable.COLUMN_DAILY_AVG_HR, dailyAvgHr);
        values.put(DatabaseHelperTable.COLUMN_DAILY_AVG_BR, dailyAvgBr);
        values.put(DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS, dailyTotalSteps);
        values.put(DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES, dailyTotalCalories);
        values.put(DatabaseHelperTable.COLUMN_DAILY_HVR, dailyAvgPace);

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateEndTimeZeroAverageData(int getId,double calories,double dailyAvgHr,double dailyAvgBr,long dailyTotalSteps, double dailyTotalCalories,  double dailyAvgPace, long endTime,int getHrAverage, int getBrAverage, double pace) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelperTable.COLUMN_WORKOUT_CALORY, calories);
        values.put(DatabaseHelperTable.COLUMN_DAILY_AVG_HR, dailyAvgHr);
        values.put(DatabaseHelperTable.COLUMN_DAILY_AVG_BR, dailyAvgBr);
        values.put(DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS, dailyTotalSteps);
        values.put(DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES, dailyTotalCalories);
        values.put(DatabaseHelperTable.COLUMN_DAILY_HVR, dailyAvgPace);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_END_TIME, endTime);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AVG_HR,getHrAverage);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AVG_BR,getBrAverage);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AVG_SPEED, pace);

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutHR(int getId, String syncHr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_HR,syncHr); //These Fields should be your String values of actual column names

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutBR(int getId, String syncBr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_BR,syncBr); //These Fields should be your String values of actual column names

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutAvgHR(int getId, int getHrAverage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AVG_HR,getHrAverage); //These Fields should be your String values of actual column names

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutAvgBR(int getId, int getBrAverage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AVG_BR,getBrAverage); //These Fields should be your String values of actual column names

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutDuration(int getId, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_DURATION,duration); //These Fields should be your String values of actual column names

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutDistance(int getId, String distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_DISTANCE,distance); //These Fields should be your String values of actual column names

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutCalory(int getId, double calory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_CALORY,calory); //These Fields should be your String values of actual column names

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutSteps(int getId, int syncSteps*//*String syncPace*//*) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelperTable.COLUMN_WORKOUT_STEPS,syncSteps);
//        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AVG_SPEED,syncPace);

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutSpeed(int getId, String syncLatLng, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelperTable.COLUMN_WORKOUT_ROUTE,syncLatLng);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_DURATION,duration);

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutLatLng(int getId, String syncLatLng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelperTable.COLUMN_WORKOUT_ROUTE,syncLatLng);

        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long updateLiveWorkOutRecord(int getId, long startTime, long endTime, String workoutType, String route*//*,String heartRate,String BreathRate,int stepsCount,*//*, double caloris, double avgSpeed, double avgHr, double avgBr, String duration, String distance, String startDate, double dailyAvgHr, double dailyAvgBr, long dailyTotalSteps, double dailyTotalCalories, double dailyHVR, boolean awsUploadStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_STEPS_TIME, startTime);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_END_TIME, endTime);
        values.put(DatabaseHelperTable.COLUMN_STEPS_DATE, workoutType);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_ROUTE, route);
//        values.put(DatabaseHelperTable.COLUMN_WORKOUT_HR, heartRate);
//        values.put(DatabaseHelperTable.COLUMN_WORKOUT_BR, BreathRate);
//        values.put(DatabaseHelperTable.COLUMN_WORKOUT_STEPS, stepsCount);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_CALORY, caloris);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AVG_SPEED, avgSpeed);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AVG_HR, avgHr);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AVG_BR, avgBr);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_DURATION, duration);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_DISTANCE, distance);
        values.put(DatabaseHelperTable.COLUMN_STEPS_VALUE, startDate);
        values.put(DatabaseHelperTable.COLUMN_DAILY_AVG_HR, dailyAvgHr);
        values.put(DatabaseHelperTable.COLUMN_DAILY_AVG_BR, dailyAvgBr);
        values.put(DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS, dailyTotalSteps);
        values.put(DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES, dailyTotalCalories);
        values.put(DatabaseHelperTable.COLUMN_DAILY_HVR, dailyHVR);
        values.put(DatabaseHelperTable.COLUMN_WORKOUT_AWS_UPLOAD_STATUS, awsUploadStatus);
        String whereClause = "id=?";
        String whereArgs[] = {String.valueOf(getId)};

        long id = db.update(DatabaseHelperTable.TABLE_NAME_STEPS, values, whereClause, whereArgs);
        db.close();
        return id;
    }

    public long insertHeartRateRawData(int heartRateRaw, String timeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_HR_VALUE, heartRateRaw);
        values.put(DatabaseHelperTable.COLUMN_HR_RAW_TIME, timeStamp);
        long id = db.insert(DatabaseHelperTable.TABLE_NAME_HR_RAW, null, values);
        db.close();
        return id;
    }

    public long insertBreathRateRawData(int heartRateRaw, String timeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperTable.COLUMN_BR_VALUE, heartRateRaw);
        values.put(DatabaseHelperTable.COLUMN_BR_RAW_TIME, timeStamp);
        long id = db.insert(DatabaseHelperTable.TABLE_NAME_BR_RAW, null, values);
        db.close();
        return id;
    }

    public ArrayList<DatabaseHelperTable> getWeeklyData(String start_date, String end_date) {
        ArrayList<DatabaseHelperTable> weeklyHistory = new ArrayList<>();

        String selectQuery = "SELECT * FROM "
                + DatabaseHelperTable.TABLE_NAME_STEPS
                + " WHERE "
                +  DatabaseHelperTable.COLUMN_STEPS_VALUE
                +" BETWEEN '"
                + end_date
                + "' AND '"
                + start_date
                + "' ORDER BY '"
                +  DatabaseHelperTable.COLUMN_STEPS_VALUE
                +"' DESC";

        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DatabaseHelperTable note = new DatabaseHelperTable();
                note.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_ID)));
                note.setStartTime(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_TIME)));
                note.setEndTime(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_END_TIME)));
                note.setWorkoutType(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_DATE)));
                note.setWorkoutStartDate(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_VALUE)));
                note.setWorkoutDistance(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_DISTANCE)));
                note.setWorkoutDuration(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_DURATION)));
                note.setAvgHeartRate(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_AVG_HR)));
                note.setAvgBreathRate(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_AVG_BR)));
                note.setHeartRate(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_HR)));
                note.setBreathRate(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_BR)));
                note.setRoute(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_ROUTE)));
                note.setStepsCount(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_STEPS)));
                note.setCalories(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_CALORY)));
                note.setPace(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_AVG_SPEED)));
                note.setDailyAvgHr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_HR)));
                note.setDailyAvgBr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_BR)));
                note.setDailyTotalSteps(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS)));
                note.setDailyTotalCalories(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES)));
                note.setDailyHVR(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_HVR)));
                weeklyHistory.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return weeklyHistory;
    }

    public ArrayList<DatabaseHelperTable> getDailyWoroutData(String start_date) {
        ArrayList<DatabaseHelperTable> weeklyHistory = new ArrayList<>();

        String selectQuery = "SELECT * FROM "
                + DatabaseHelperTable.TABLE_NAME_STEPS
                + " WHERE "
                +  DatabaseHelperTable.COLUMN_STEPS_VALUE
                + " = '"
                + start_date
                + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DatabaseHelperTable note = new DatabaseHelperTable();
                note.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_ID)));
                note.setStartTime(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_TIME)));
                note.setEndTime(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_END_TIME)));
                note.setWorkoutType(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_DATE)));
                note.setWorkoutStartDate(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_VALUE)));
                note.setWorkoutDistance(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_DISTANCE)));
                note.setWorkoutDuration(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_DURATION)));
                note.setAvgHeartRate(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_AVG_HR)));
                note.setAvgBreathRate(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_AVG_BR)));
                note.setHeartRate(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_HR)));
                note.setBreathRate(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_BR)));
                note.setRoute(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_ROUTE)));
                note.setStepsCount(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_STEPS)));
                note.setCalories(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_CALORY)));
                note.setPace(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_AVG_SPEED)));
                note.setDailyAvgHr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_HR)));
                note.setDailyAvgBr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_BR)));
                note.setDailyTotalSteps(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS)));
                note.setDailyTotalCalories(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES)));
                note.setDailyHVR(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_HVR)));
                weeklyHistory.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return weeklyHistory;
    }

    public ArrayList<DatabaseHelperTable> getWeeklyHrBrAvgData(String start_date) {
            ArrayList<DatabaseHelperTable> dailyHrAvgData = new ArrayList<>();
            try {
                SQLiteDatabase db = getReadableDatabase();
//        select max(workout_start_date_time) from LIVE_WORKOUT where workout_start_date='07/25/2019'
                String sql = "select max(" + DatabaseHelperTable.COLUMN_STEPS_TIME
                        + ") "
                        + "," + DatabaseHelperTable.COLUMN_DAILY_AVG_HR
                        + "," + DatabaseHelperTable.COLUMN_DAILY_AVG_BR
                        + " from "
                        + DatabaseHelperTable.TABLE_NAME_STEPS
                        + " where "
                        + DatabaseHelperTable.COLUMN_STEPS_VALUE
                        + " = '"
                        + start_date
                        + "'";

                Cursor cursor = db.rawQuery(sql, null*//*new String[]{start_date}*//*);

                if (cursor.moveToFirst()) {
                    do {
                        DatabaseHelperTable note = new DatabaseHelperTable();
                        note.setDailyAvgHr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_HR)));
                        note.setDailyAvgBr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_BR)));
                        dailyHrAvgData.add(note);
                    } while (cursor.moveToNext());
                }
                db.close();
            } catch (SQLiteCantOpenDatabaseException se) {
                se.printStackTrace();
            }
            return dailyHrAvgData;
    }

    public ArrayList<DatabaseHelperTable> getDailyAvgData(String start_date) {
        ArrayList<DatabaseHelperTable> dailyHrAvgData = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
//        select max(workout_start_date_time) from LIVE_WORKOUT where workout_start_date='07/25/2019'
        String sql = "select max(" + DatabaseHelperTable.COLUMN_STEPS_TIME
                + ") "
                + "," + DatabaseHelperTable.COLUMN_STEPS_ID
                + "," + DatabaseHelperTable.COLUMN_DAILY_AVG_HR
                + "," + DatabaseHelperTable.COLUMN_DAILY_AVG_BR
                + "," + DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS
                + "," + DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES
                + "," + DatabaseHelperTable.COLUMN_DAILY_HVR
                + " from "
                + DatabaseHelperTable.TABLE_NAME_STEPS
                + " where "
                + DatabaseHelperTable.COLUMN_STEPS_VALUE
                + " = '"
                + start_date
                + "'";

        Cursor cursor = db.rawQuery(sql, null*//*new String[]{start_date}*//*);

        if (cursor.moveToFirst()) {
            do {
                DatabaseHelperTable note = new DatabaseHelperTable();
                note.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_ID)));
                note.setDailyAvgHr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_HR)));
                note.setDailyAvgBr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_BR)));
                note.setDailyTotalSteps(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS)));
                note.setDailyTotalCalories(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES)));
                note.setDailyHVR(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_HVR)));
                dailyHrAvgData.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return dailyHrAvgData;
    }

    public ArrayList<DatabaseHelperTable> getMonthlyData(String start_date, String end_date) {
        ArrayList<DatabaseHelperTable> monthlyHistory = new ArrayList<>();

        String selectQuery = "select max(" + DatabaseHelperTable.COLUMN_STEPS_TIME
                + ") "
                + "," + DatabaseHelperTable.COLUMN_STEPS_ID
                + "," + DatabaseHelperTable.COLUMN_DAILY_AVG_HR
                + "," + DatabaseHelperTable.COLUMN_DAILY_AVG_BR
                + "," + DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS
                + "," + DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES
                + "," + DatabaseHelperTable.COLUMN_DAILY_HVR
                + " from "
                + DatabaseHelperTable.TABLE_NAME_STEPS
                + " WHERE "
                +  DatabaseHelperTable.COLUMN_STEPS_VALUE
                +" BETWEEN '"
                + end_date
                + "' AND '"
                + start_date
                + "' ORDER BY '"
                +  DatabaseHelperTable.COLUMN_STEPS_VALUE
                +"' ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DatabaseHelperTable note = new DatabaseHelperTable();
                note.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_ID)));
                note.setDailyAvgHr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_HR)));
                note.setDailyAvgBr(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_AVG_BR)));
                note.setDailyTotalSteps(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_TOTAL_STEPS)));
                note.setDailyTotalCalories(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_TOTAL_CALORIES)));
                note.setDailyHVR(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_DAILY_HVR)));
                monthlyHistory.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return monthlyHistory;
    }

    //    SELECT count(*) FROM LIVE_WORKOUT;
    public BigDecimal getTotalRecordCount() {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select count(*) from " + DatabaseHelperTable.TABLE_NAME_STEPS;

        Cursor cursor = db.rawQuery(sql, null);

        try {
            cursor.moveToFirst();
            String sum = cursor.getString(0);

            if (sum == null)
                return new BigDecimal("0");
            return new BigDecimal(sum);
        } finally {
            cursor.close();
        }
    }

    public BigDecimal getWeeklyBrAvgData(String start_date) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select avg(" + DatabaseHelperTable.COLUMN_WORKOUT_AVG_BR + ") " +
                "from " + DatabaseHelperTable.TABLE_NAME_STEPS + " " +
                "where " + DatabaseHelperTable.COLUMN_STEPS_VALUE + " = '" + start_date + "'";

        Cursor cursor = db.rawQuery(sql, null*//*new String[]{start_date}*//*);

        try {
            cursor.moveToFirst();
            String sum = cursor.getString(0);

            if (sum == null)
                return new BigDecimal("0");
            return new BigDecimal(sum);
        } finally {
            cursor.close();
        }
    }

    public String getStartDateOfRowOne() {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select " + DatabaseHelperTable.COLUMN_STEPS_VALUE + " " +
                "from " + DatabaseHelperTable.TABLE_NAME_STEPS + " " +
                " ORDER BY " + DatabaseHelperTable.COLUMN_STEPS_ID + " " + "ASC LIMIT 1";

        Cursor cursor = db.rawQuery(sql, null*//*new String[]{start_date}*//*);
        String sum = "";
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                sum = cursor.getString(0);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        if (sum == null || sum.equalsIgnoreCase(""))
            return "";
        return (sum);
    }

    public ArrayList<DatabaseHelperTable> getAllLiveWorkOutData() {
        ArrayList<DatabaseHelperTable> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DatabaseHelperTable.TABLE_NAME_STEPS + " ORDER BY " + DatabaseHelperTable.COLUMN_STEPS_TIME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DatabaseHelperTable note = new DatabaseHelperTable();
                note.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_ID)));
                note.setStartTime(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_TIME)));
                note.setEndTime(cursor.getLong(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_END_TIME)));
                note.setWorkoutType(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_DATE)));
                note.setWorkoutStartDate(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_STEPS_VALUE)));
                note.setWorkoutDistance(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_DISTANCE)));
                note.setWorkoutDuration(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_DURATION)));
                note.setAvgHeartRate(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_AVG_HR)));
                note.setAvgBreathRate(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_AVG_BR)));
                note.setHeartRate(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_HR)));
                note.setBreathRate(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_BR)));
                note.setRoute(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_ROUTE)));
                note.setStepsCount(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_STEPS)));
                note.setCalories(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_CALORY)));
                note.setPace(cursor.getDouble(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_WORKOUT_AVG_SPEED)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }

    public ArrayList<DatabaseHelperTable> getAllHRawData() {
        ArrayList<DatabaseHelperTable> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DatabaseHelperTable.TABLE_NAME_HR_RAW *//*+ " ORDER BY " + Note.COLUMN_TIMESTAMP + " DESC"*//*;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DatabaseHelperTable note = new DatabaseHelperTable();
                note.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_HR_RAW_ID)));
                note.setHeartRateRaw(cursor.getInt(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_HR_VALUE)));
                note.setTimeStamp(cursor.getString(cursor.getColumnIndex(DatabaseHelperTable.COLUMN_HR_RAW_TIME)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }

    */
}
