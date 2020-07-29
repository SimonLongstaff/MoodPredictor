package com.example.moodpredictor;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ScreenTimeBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "ScreenTimeBroadcastRece";
    private long startTimer;
    private long screenOnTime;
    private int currentUser;
    private String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    DatabaseHelper databaseHelper;



    @SuppressLint("DefaultLocale")
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG,"onTimeReceiver");

        
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            startTimer = System.currentTimeMillis();
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            long endTimer =System.currentTimeMillis();
            long screenOnTimeSingle = endTimer - startTimer;
            screenOnTime = Long.parseLong(databaseHelper.getOnTime(currentUser,date));

            long TIME_ERROR = 1000;
            if (screenOnTimeSingle > TIME_ERROR){
                System.out.println(screenOnTimeSingle);
                screenOnTime += (screenOnTimeSingle/1000) % 60;

                if (databaseHelper.onTimeExists(currentUser,date)){
                    databaseHelper.updateOnTime(currentUser,date, (int) screenOnTime);
                }else
                    databaseHelper.newOntime(currentUser,date, (int) screenOnTime);
            }



        }
        System.out.println("Screen on Time: " + screenOnTime);

    }

    public void setLoggedinUser(int user){
        currentUser = user;
    }

    public void setDatabaseHelper(DatabaseHelper setdatabaseHelper){
        databaseHelper=setdatabaseHelper;
    }
}
