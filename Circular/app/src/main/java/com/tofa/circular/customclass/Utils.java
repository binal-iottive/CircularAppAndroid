package com.tofa.circular.customclass;

import android.content.Context;
import android.content.Intent;

import com.tofa.circular.model.AppAlertModel;
import com.tofa.circular.sqldatabase.DatabaseHelperTable;
import com.tofa.circular.sqldatabase.InsertDataService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static ArrayList<AppAlertModel> appAlertArraylist = new ArrayList<>();
    public static ArrayList<DatabaseHelperTable> insertDataArrayList = new ArrayList<>();
    public static final int REQUEST_ADD_ALERT = 1;
    public static final int REQUEST_CONTACT_PICKER= 2;
    public static final String dateFormate= "dd-MMM-yyyy";

    public static String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat(dateFormate);
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static long getCurrentTime(){
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
}
