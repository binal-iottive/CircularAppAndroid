package com.tofa.circular.customclass;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tofa.circular.model.AppAlertModel;
import com.tofa.circular.sqldatabase.DatabaseHelperTable;
import com.tofa.circular.sqldatabase.InsertDataService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Utils {
    public static ArrayList<AppAlertModel> appAlertArraylist = new ArrayList<>();
    public static ArrayList<DatabaseHelperTable> insertDataArrayList = new ArrayList<>();
    public static final int REQUEST_ADD_ALERT = 1;
    public static final int REQUEST_CONTACT_PICKER = 2;
    public static final String dateFormate = "dd-MMM-yyyy";

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat(dateFormate);
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static void startInsertDataService(Context context) {
        stopInsertDataService(context);
        try {
            context.startService(new Intent(context, InsertDataService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Method to stop the service
    public static void stopInsertDataService(Context context) {
        try {
            context.stopService(new Intent(context, InsertDataService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getPastWeekDate() {
        String[] datesList = new String[7];
        Calendar c = GregorianCalendar.getInstance();
        System.out.println("Current week = " + Calendar.DAY_OF_WEEK);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        System.out.println("Current week = " + Calendar.DAY_OF_WEEK);
        DateFormat df = new SimpleDateFormat(dateFormate, Locale.getDefault());

        Calendar previousWeekCalendar = c;
        previousWeekCalendar.add(Calendar.DAY_OF_YEAR, -7);
        String startPreviousWeekDate = df.format(previousWeekCalendar.getTime());
        datesList[0] = startPreviousWeekDate;
        for (int i = 1; i < 7; i++) {
            previousWeekCalendar.add(Calendar.DATE, 1);
            String endPreviousWeekDate = df.format(previousWeekCalendar.getTime());
            datesList[i] = endPreviousWeekDate;
        }
        Log.d("getPastWeekDate==>", Arrays.toString(datesList));
        return datesList;
    }

    public static String[] getLastMonthWeekDate() {
 /*       SimpleDateFormat format = new SimpleDateFormat(dateFormate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Log.d("getLastMonthWeekDate==>", format.format(cal.getTime()));*/
        String[] datesList = new String[5];
        Calendar c = GregorianCalendar.getInstance();
        System.out.println("Current week = " + Calendar.DAY_OF_WEEK);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        System.out.println("Current week = " + Calendar.DAY_OF_WEEK);
        DateFormat df = new SimpleDateFormat(dateFormate, Locale.getDefault());

        Calendar previousWeekCalendar = c;
        previousWeekCalendar.add(Calendar.DAY_OF_YEAR, -28);
        String startPreviousWeekDate = df.format(previousWeekCalendar.getTime());
        datesList[0] = startPreviousWeekDate;
        for (int i = 1; i < 4; i++) {
            previousWeekCalendar.add(Calendar.DATE, 7);
            String endPreviousWeekDate = df.format(previousWeekCalendar.getTime());
            datesList[i] = endPreviousWeekDate;
        }
        datesList[4] = getCurrentDate();
        Log.d("getLastMonthWeekDate==>", Arrays.toString(datesList));
        return datesList;
    }

    public static String[] getLastMonthAllDate() {
        String[] datesList = new String[28];
        Calendar c = GregorianCalendar.getInstance();
        System.out.println("Current week = " + Calendar.DAY_OF_WEEK);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        System.out.println("Current week = " + Calendar.DAY_OF_WEEK);
        DateFormat df = new SimpleDateFormat(dateFormate, Locale.getDefault());

        Calendar previousWeekCalendar = c;
        previousWeekCalendar.add(Calendar.DAY_OF_YEAR, -28);
        String startPreviousWeekDate = df.format(previousWeekCalendar.getTime());
        datesList[0] = startPreviousWeekDate;
        for (int i = 1; i < 28; i++) {
            previousWeekCalendar.add(Calendar.DATE, 1);
            String endPreviousWeekDate = df.format(previousWeekCalendar.getTime());
            datesList[i] = endPreviousWeekDate;
        }
        Log.d("getLastMonthWeekDate==>", Arrays.toString(datesList));
        return datesList;
    }

    public static long dateToMS(String givenDateString){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormate);
        try {
            Date mDate = sdf.parse(givenDateString);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis();
    }
}
