package su.zzz.android.currencysalemonitormgn.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import su.zzz.android.currencysalemonitormgn.MonitorActivity;
import su.zzz.android.currencysalemonitormgn.R;
import su.zzz.android.currencysalemonitormgn.database.*;

public class MonitorWidget extends AppWidgetProvider {
    private static final String TAG = MonitorWidget.class.getSimpleName();
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
//            Intent intent = new Intent(context, MonitorActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_monitor);
            views.setTextViewText(R.id.tv_usd_course, String.format("%.2f", MonitorDbHelper.getInstance(context).getMinCourse(MonitorDbSchema.CourseTable.Cols.USD)));
            views.setTextViewText(R.id.tv_eur_course, String.format("%.2f", MonitorDbHelper.getInstance(context).getMinCourse(MonitorDbSchema.CourseTable.Cols.EUR)));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        Log.i(TAG, "onUpdate");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), MonitorWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}
