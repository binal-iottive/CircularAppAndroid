package com.tofa.circular.customclass;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.tofa.circular.model.AlarmModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SharedPref {

    @Nullable
    private static SharedPreferences sharedPreferences = null;

    public final static String ALARM_LIST= "alarm_list";



    public static void openPref(@NonNull Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Nullable
    public static String getValue(@NonNull Context context, String key, String defaultValue) {
        String result="";
        try {
            SharedPref.openPref(context);
             result = SharedPref.sharedPreferences.getString(key, defaultValue);
            SharedPref.sharedPreferences = null;
        }catch (Exception e){

        }

        return result;
    }

    @Nullable
    public static int getValue(@NonNull Context context, String key, int defaultValue) {
        int result = -1;
        try {
            SharedPref.openPref(context);
             result = SharedPref.sharedPreferences.getInt(key, defaultValue);
            SharedPref.sharedPreferences = null;
        }catch (Exception e){

        }
        return result;
    }

    @Nullable
    public static Long getValue(@NonNull Context context, String key, Long defaultValue) {
        SharedPref.openPref(context);
        Long result = SharedPref.sharedPreferences.getLong(key, defaultValue);
        SharedPref.sharedPreferences = null;
        return result;
    }

    public static void setValue(@NonNull Context context, String key, String value) {
        try {
            SharedPref.openPref(context);
            Editor prefsPrivateEditor = SharedPref.sharedPreferences.edit();
            prefsPrivateEditor.putString(key, value);
            prefsPrivateEditor.commit();
            prefsPrivateEditor = null;
            SharedPref.sharedPreferences = null;
        }catch (Exception e){

        }
    }

    public static void setValue(@NonNull Context context, String key, int value) {
        try {
            SharedPref.openPref(context);
            Editor prefsPrivateEditor = SharedPref.sharedPreferences.edit();
            prefsPrivateEditor.putInt(key, value);
            prefsPrivateEditor.commit();
            prefsPrivateEditor = null;
            SharedPref.sharedPreferences = null;
        }catch (Exception e){

        }
    }

    public static void setValue(@NonNull Context context, String key, Long value) {
        try {
            SharedPref.openPref(context);
            Editor prefsPrivateEditor = SharedPref.sharedPreferences.edit();
            prefsPrivateEditor.putLong(key, value);
            prefsPrivateEditor.commit();
            prefsPrivateEditor = null;
            SharedPref.sharedPreferences = null;
        }catch (Exception e){}
    }


    public static ArrayList<AlarmModel> getAlarmList(Context context, String key) {
        ArrayList<AlarmModel> value = new ArrayList<AlarmModel>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            try {
                value = ( ArrayList<AlarmModel>) ObjectSerializer.deserialize(preferences.getString(key, ObjectSerializer.serialize(new  ArrayList<AlarmModel>())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static boolean setAlarmList(Context context, String key, ArrayList<AlarmModel> value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            Editor editor = preferences.edit();
            try {
                editor.putString(key, ObjectSerializer.serialize(value));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return editor.commit();
        }
        return false;
    }



}
