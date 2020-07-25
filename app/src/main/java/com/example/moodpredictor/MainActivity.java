package com.example.moodpredictor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        SensorEventListener,
        StepListener{

    //Drawers
    private DrawerLayout drawer;

    //Var
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

   //Location

    //Sensors
    private SensorManager sensorManager;
    private StepDetector stepDetector;
    private Sensor accel;
    private int Stepsnum;
    private int shakeNum;
    DatabaseHelper database = new DatabaseHelper(this);

    //User
    private int loggedInUID = 1;
    private String loggedInName;
    String id = loggedInUID + date;


    //Debug User
    User debug = new User("Simon");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Step Sensor setup
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_NORMAL);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);
        Stepsnum = 0;
        sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);




        //Navigation Bar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //User
       loggedInUID = 1;
       //database.insertUser(debug);
      //database.dummyData();
       database.newShake(1,date);
       SettingUser();

       //Create Steps database
        if (!database.stepIDExists(id)) {
            System.out.println("It returned false for Step ID exists");
            database.insertNewStepDay(0,loggedInUID);
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }



    }



    //User
    public int getLoggedInUser(){
        return loggedInUID;
    }

    public void setLoggedInUser(int loggedInUser) {
        this.loggedInUID = loggedInUser;
    }

    public void SettingUser(){
        String user = database.getLoggedIn(loggedInUID);
        System.out.println(user);
       loggedInName = user;
    }

    public String getLoggedInName(){
        return loggedInName;
    }

    public String getDate(){return date;}


    //Navigation Bar

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_steps:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
                break;
            case R.id.nav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Step Sensor updates

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                stepDetector.updateAccel(
                        sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                break;

            }

            case  Sensor.TYPE_STEP_COUNTER: {
                if (Stepsnum < 1)
                    Stepsnum = (int) sensorEvent.values[0];

                Stepsnum = (int) sensorEvent.values[0] - Stepsnum;
                database.updateSteps(id, Stepsnum);
                System.out.println("New Steps registered");
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    //Registers the fragment and updates the
    @Override
    public void step(long timeNs) {
        Stepsnum++;
        System.out.println("Old Step registered");
        database.updateSteps(id,Stepsnum);

    }


    public String getSteps(){
        String id = loggedInUID + date;
        System.out.println(id);
        return database.getSteps(id);
    }

    public String getShake(){
       return database.getShake(loggedInUID, date);
    }

    @Override
    public void shake(long timeNs) {
        System.out.println("New Shake");
        if (database.shakeExists(getLoggedInUser(),date)){
            shakeNum++;
            database.updateShake(getLoggedInUser(),date,shakeNum);
        }
        else {
            database.newShake(loggedInUID, date);
            shakeNum = 1;
        }


    }
}