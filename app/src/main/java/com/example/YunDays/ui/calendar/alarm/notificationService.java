package com.example.YunDays.ui.calendar.alarm;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.YunDays.MainActivity;
import com.example.YunDays.R;
import com.example.YunDays.event.dayEvent;
import com.example.YunDays.sqlite.EventSQLiteOperation;

import java.util.Calendar;
import java.util.List;

public class notificationService extends Service {

    private static NotificationManager notificationManager;
    private List<dayEvent> dayEvents;
    @Override
    public void onCreate() {
        if(notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String ChannelId = "DayEventNotification";
            String ChannelName = "日程提醒";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(notificationService.this, ChannelId, ChannelName, importance);
        }
        Calendar c = Calendar.getInstance();
        @SuppressLint("DefaultLocale") String today = String.format("%d-%d-%d",
                c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1),
                c.get(Calendar.DAY_OF_MONTH));
        EventSQLiteOperation operation = new EventSQLiteOperation();
        dayEvents = operation.searchDayEvent(this, today);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context, String ChannelId,
                                                  String ChannelName, int importance) {
        NotificationChannel channel = new NotificationChannel(ChannelId, ChannelName, importance);
        notificationManager.createNotificationChannel(channel);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!(dayEvents.size() == 0)) {
            Intent intent_jump = new Intent(this, MainActivity.class);
            intent_jump.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 ,
                    intent_jump, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "DayEventNotification")
                    .setSmallIcon(R.drawable.ic_baseline_yundays_24)
                    .setContentTitle("云日程提醒")
                    .setContentText("您今天有未执行的日程哦！")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(1, builder.build());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
