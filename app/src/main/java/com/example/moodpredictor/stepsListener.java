package com.example.moodpredictor;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class stepsListener extends Service implements StepListener, SensorEventListener{

    DatabaseHelper database;
    StepDetector stepDetector;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static final String CHANNEL_ID = "ServiceChannel";

    @Override
    public void onCreate() {
        database = DatabaseHelper.getInstance(this);
        SensorManager sensorManger = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accel = sensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);
        sensorManger.registerListener(this,accel,SensorManager.SENSOR_DELAY_NORMAL);

        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = "Using Sensors";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Data Gathering Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_baseline_add_location_24)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void step(long timeNs) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        int steps = Integer.parseInt(database.getSteps(date,1));
        System.out.println("Reciever Step registered");
        steps++;
        database.updateSteps(date, 1, steps);
    }

    @Override
    public void shake(long timeNs) {
        System.out.println("Reciever Shake");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (database.shakeExists(1, date)) {
            int shake = Integer.parseInt(database.getShake(1,date));
            shake++;
            database.updateShake(1, date, shake);
        } else {
            database.newShake(1, date, 1);
        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            stepDetector.updateAccel(
                    sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
