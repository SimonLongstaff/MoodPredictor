package com.example.moodpredictor;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class listeners extends Service implements SensorEventListener {


    private String date;
    private String uId;
    DatabaseHelper databaseHelper;
    ArrayList<Location> userLocations;
    LocationListener locationListener;
    private static final int Geo_Radius = 50;
    double startTimer;
    double endTimer;
    Location currentLocation = null;
    ScreenTimeBroadcastReceiver screenTimeBroadcastReceiver;
    IntentFilter lockfilter;

    @Override
    public ComponentName startService(Intent service) {
        date = service.getStringExtra("Date");
        uId = service.getStringExtra("uID");
        return super.startService(service);
    }

    public static final String CHANNEL_ID = "exampleServiceChannel";

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        userLocations = new ArrayList<>();
        updateAreas();

        String input = "GPS";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_baseline_add_location_24)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);


        return START_REDELIVER_INTENT;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {


        databaseHelper = DatabaseHelper.getInstance(this);
        screenTimeBroadcastReceiver = new ScreenTimeBroadcastReceiver();
        lockfilter = new IntentFilter();
        lockfilter.addAction(Intent.ACTION_SCREEN_ON);
        lockfilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenTimeBroadcastReceiver, lockfilter);
        screenTimeBroadcastReceiver.setLoggedinUser(1);
        screenTimeBroadcastReceiver.setDatabaseHelper(databaseHelper);


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLatitude();
                updateAreas();
                System.out.println("Lat: " + latitude + " Long: " + longitude);
                if (currentLocation != null) {
                    if (location.distanceTo(currentLocation) > Geo_Radius) {
                        endTimer = System.currentTimeMillis();
                        int time = (int) ((endTimer - startTimer) / 1000);
                        int lID = databaseHelper.getLocID(1, currentLocation.getProvider());
                        if (databaseHelper.visitExists(lID, date)) {

                            databaseHelper.updateVisit(lID, time, date);
                        } else
                            databaseHelper.newVisit(lID, time, date);
                        currentLocation = null;
                    }
                } else {
                    for (int i = 0; i < userLocations.size(); i++) {
                        if (location.distanceTo(userLocations.get(i)) < Geo_Radius) {
                            currentLocation = userLocations.get(i);
                            startTimer = System.currentTimeMillis();
                        }
                    }
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenTimeBroadcastReceiver);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void updateAreas() {
        userLocations = databaseHelper.getLocations(1);
    }
}
