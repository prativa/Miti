package com.bibta.miti;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MitiWidgetProvider extends AppWidgetProvider {
    public static void updateWidget(Context context, RemoteViews remoteViews) {
        Calendar calendar = Calendar.getInstance();

        // time
        String time = new SimpleDateFormat("hh:mm a", Locale.US).format(calendar.getTime());
        remoteViews.setTextViewText(R.id.time, time);

        // english date
        String engDate = calendar.get(Calendar.DATE)+"";
        remoteViews.setTextViewText(R.id.engDate, engDate);
        String engMonthYear = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                + "\n" + calendar.get(Calendar.YEAR);
        remoteViews.setTextViewText(R.id.engMonthYear, engMonthYear);

        // day
        String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        remoteViews.setTextViewText(R.id.day, day);

        // TODO: Nepali date


        // Set alarm for update in next minute
        int timeTillNextMinute = (60 - calendar.get(Calendar.SECOND)) * 1000;
        Intent intent = new Intent(context, MitiWidgetProvider.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
        alarm.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + timeTillNextMinute, pendingIntent);
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        ComponentName thisWidget = new ComponentName(context, MitiWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_miti);
            updateWidget(context, remoteViews);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
