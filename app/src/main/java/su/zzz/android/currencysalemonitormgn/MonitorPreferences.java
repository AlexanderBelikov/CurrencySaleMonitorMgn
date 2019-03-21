package su.zzz.android.currencysalemonitormgn;

import android.content.Context;
import android.preference.PreferenceManager;

public class MonitorPreferences {
    private static final String PREF_COURSE_FETCH_SUCCESS = "course_fetch_success";
    private static final String PREF_COURSE_FETCH_DATE = "course_fetch_date";

    private static final String PREF_USD_EXPECTED_COURSE = "usd_expected_course";
    private static final String PREF_USD_MONITOR_STATE = "usd_monitor_state";
    private static final String PREF_EUR_EXPECTED_COURSE = "eur_expected_course";
    private static final String PREF_EUR_MONITOR_STATE = "eur_monitor_state";


    public static boolean getCourseFetchSuccess(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_COURSE_FETCH_SUCCESS,false);
    }
    public static void setCourseFetchSuccess(Context context, boolean success){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_COURSE_FETCH_SUCCESS, success)
                .apply();
    }

    public static long getCourseFetchDate(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(PREF_COURSE_FETCH_DATE,0);
    }
    public static void setCourseFetchDate(Context context, long date){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(PREF_COURSE_FETCH_DATE, date)
                .apply();
    }

    public static boolean getUsdMonitorState(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_USD_MONITOR_STATE,false);
    }
    public static void setUsdMonitorState(Context context, boolean state){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_USD_MONITOR_STATE, state)
                .apply();
    }

    public static boolean getEurMonitorState(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_EUR_MONITOR_STATE,false);
    }
    public static void setEurMonitorState(Context context, boolean state){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_EUR_MONITOR_STATE, state)
                .apply();
    }

    public static float getUsdExpectedCourse(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(PREF_USD_EXPECTED_COURSE,0);
    }
    public static void setUsdExpectedCourse(Context context, float price){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putFloat(PREF_USD_EXPECTED_COURSE, price)
                .apply();
    }

    public static float getEurExpectedCourse(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(PREF_EUR_EXPECTED_COURSE,0);
    }
    public static void setEurExpectedCourse(Context context, float price){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putFloat(PREF_EUR_EXPECTED_COURSE, price)
                .apply();
    }
}
