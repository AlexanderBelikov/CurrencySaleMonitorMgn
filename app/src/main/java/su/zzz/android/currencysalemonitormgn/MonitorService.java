package su.zzz.android.currencysalemonitormgn;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.concurrent.TimeUnit;

public class MonitorService extends IntentService {
    private static final String TAG = MonitorService.class.getSimpleName();
    public static final long POOL_INTERVAL_MS = TimeUnit.SECONDS.toMillis(15);
    public static final String ACTION_UPDATE_COURSE = "su.zzz.android.currencysalemonitormgn.UPDATE_COURSE";
    public MonitorService() {
        super(TAG);
    }
    public static Intent newIntent(Context context) {
        return new Intent(context, MonitorService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(!isNetworkAvailableAndConnected()){
            return;
        }
        try {
            new CourseFetcher().fetch(getApplicationContext());
            MonitorPreferences.setCourseFetchDate(getApplicationContext(), System.currentTimeMillis());
            MonitorPreferences.setCourseFetchSuccess(getApplicationContext(), true);
            sendBroadcast(new Intent(ACTION_UPDATE_COURSE));
        } catch (Exception e) {
            MonitorPreferences.setCourseFetchDate(getApplicationContext(), System.currentTimeMillis());
            MonitorPreferences.setCourseFetchSuccess(getApplicationContext(), false);
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Log.i(TAG, "setServiceAlarm: "+isOn);
        Intent i = MonitorService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(isOn){
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POOL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context){
        Intent i = newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
}
