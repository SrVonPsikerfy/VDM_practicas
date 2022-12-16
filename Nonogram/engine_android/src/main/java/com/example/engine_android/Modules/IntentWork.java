package com.example.engine_android.Modules;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class IntentWork extends Worker {

    Context context;

    public IntentWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        //Intent intent = new Intent(context , MainActivity.);
        //PendingIntent contentIntent = PendingIntent. getActivity(context, 0, intent, PendingIntent. FLAG_UPDATE_CURRENT);
        Data data = getInputData();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, data.getString("chanel"))
                .setSmallIcon(data.getInt("smallIcon", androidx.constraintlayout.widget.R.drawable.notification_template_icon_low_bg))
                .setContentTitle( "My notification" )
                .setContentText( "Much longer text that cannot fit one line..." )
                .setStyle( new NotificationCompat.BigTextStyle()
                        .bigText( "Much longer text that cannot fit one line..." ))
                .setPriority(NotificationCompat. PRIORITY_DEFAULT)
                //.setContentIntent(contentIntent)
                .setAutoCancel(true);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());

        return Result.success();
    }
}