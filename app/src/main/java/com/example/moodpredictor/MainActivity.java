package com.example.moodpredictor;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener {

    //Variables
    private DrawerLayout drawer;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    DatabaseHelper database = new DatabaseHelper(this);
    public static final String CHANNEL_ID = "ServiceChannel";
    ArrayList<Location> userLocations;
    private int loggedInUID = 1;

    /**
     * Initial launch
     * @param savedInstanceState -
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.FOREGROUND_SERVICE}, 3);
        }

        //Launches the services that keep track of stats
        Intent intent = new Intent(this, listeners.class);
        intent.putExtra("Date", getDate());
        intent.putExtra("uID", String.valueOf(getLoggedInUser()));
        startForegroundService(intent);

        startForegroundService(new Intent(this, stepsListener.class));


        //Navigation Bar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //Check if user exists in the database, launch the new user screen if they do not
        if (database.getLoggedIn() == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WelcomeFragment()).commit();
        } else {

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        }

    }

    /**
     * Get current user
     * @return - user id
     */
    public int getLoggedInUser() {
        return loggedInUID;
    }

    /**
     * Sets the current logged in user
     * @param loggedInUser - user
     */
    public void setLoggedInUser(int loggedInUser) {
        this.loggedInUID = loggedInUser;
    }

    /**
     * Gets current date
     * @return - today's date
     */
    public String getDate() {
        return date;
    }

    /**
     * Navigation back
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    /**
     * Navigation menu selections
     * @param item
     * @return
     */
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
                break;
            case R.id.nav_listloc:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditLocationFragment()).commit();
                break;
            case R.id.nav_loc:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocationRatingFragment()).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Updates the user area list from the database
     */
    public void updateAreas() {
        userLocations = database.getLocations(getLoggedInUser());
    }

    /**
     * Generates a list of LatLngs for user locations from database
     * @return - list of Latlngs of locations
     */
    public ArrayList<LatLng> getUserLocLatLan() {
        ArrayList<LatLng> retval = new ArrayList<>();
        for (int i = 0; i < userLocations.size(); i++) {
            retval.add(new LatLng(userLocations.get(i).getLatitude(), userLocations.get(i).getLongitude()));
        }
        return retval;
    }

    /**
     * Generates a list of the names of user locations from the database
     * @return - names of all user locations
     */
    public ArrayList<String> getUserLocations() {
        ArrayList<String> retval = new ArrayList<>();
        for (int i = 0; i < userLocations.size(); i++) {
            retval.add(userLocations.get(i).getProvider());
        }
        return retval;
    }

    /**
     * Function to display a toast when permission is granted
     * @param requestCode -
     * @param permissions -
     * @param grantResults -
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 3) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }




    }

    /**
     * Returns the number of steps for the current date
     * @return - the number of steps for the current date
     */
    public String getSteps() {
        return database.getSteps(date, getLoggedInUser());
    }

    /**
     * Returns the bucketed value of the shakes for the current date
     * @return - shake value bucketed
     */
    public int getShakeBucketed() {
        return BayesHelper.shakeBucket(Integer.parseInt(database.getShake(getLoggedInUser(), getDate())));

    }

    /**
     * Build a matrix of step and mood values for Bayesian prediction
     * @return - a matrix of steps per mood
     */
    public int[][] stepsMatrixBuilder() {

        ArrayList<StepMoodObject> matrixBuilder = database.getStepsMood(getLoggedInUser());
        System.out.println(matrixBuilder.size());

        int[][] matrix = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            //System.out.println(i);
            int steps = matrixBuilder.get(i).getSteps();
            int mood = matrixBuilder.get(i).getMood() - 1;
            int stepbucket = BayesHelper.stepBucket(steps);


            matrix[mood][stepbucket]++;
        }

        System.out.println("Steps: " + Arrays.deepToString(matrix));
        return matrix;
    }

    /**
     * Builds a matrix of shake and mood values for prediction
     * @return - A matrix with values of shakes per mood
     */
    public int[][] shakeMatrixBuilder() {
        ArrayList<ShakeMoodObject> matrixBuilder = database.getShakeMood(getLoggedInUser());

        int[][] matrix = {{1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            int shakes = matrixBuilder.get(i).getShakes();
            int mood = matrixBuilder.get(i).getMood() - 1;
            int shakeBucket = BayesHelper.shakeBucket(shakes);


            matrix[mood][shakeBucket]++;
        }
        System.out.println("Shake: " + Arrays.deepToString(matrix));
        return matrix;

    }

    /**
     * Builds the matrix of screen on time and mood for prediction
     * @return - matrix of values of screen on time per mood
     */
    public int[][] onTimeMAtrixBuilder() {

        ArrayList<onTimeMoodObject> matrixBuilder = database.getonTimeMood(getLoggedInUser());

        int[][] matrix = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            System.out.println(i);
            System.out.println(matrixBuilder.get(i).toString());
            int onTime = matrixBuilder.get(i).getOnTime();
            int mood = matrixBuilder.get(i).getMood() - 1;
            int onTimeBucket = BayesHelper.onTimeBucket(onTime);

            matrix[mood][onTimeBucket]++;

        }
        return matrix;
    }

    /**
     * Builds a matrix with time spent at location and mood values for prediction
     * @param lID - the location ID
     * @return - Matrix of values of time spent at location per mood
     */
    public int[][] locMatrixBuilder(int lID) {

        ArrayList<LocationMoodObject> matrixBuilder = database.getLocMood(lID);

        int[][] matrix = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            System.out.println(matrixBuilder.get(i).toString());
            int mood = matrixBuilder.get(i).getMood() - 1;
            int vTime = matrixBuilder.get(i).getTime();
            int vTimeBucket = BayesHelper.dayBucket(vTime);

            matrix[mood][vTimeBucket]++;

        }
        return matrix;
    }

    /** Location and mood value calculations
     *  Builds a matrix for each location the user has taking in the time visited each day and the mood
     *  Gets the total for each mood from each location matrix and adds them to a running total for the current day
     *  Does the same for each location inclusive of all dates in database
     *
     *  Creates an object with all these variables that can be handed off to prediction function.
     *
     * @param date - todays date
     * @return - an object that includes all the details of the location dated needed for the prediction of mood.
     */
    public LocMoodCalObject locMoodCalculation(String date) {
        updateAreas();
        double locMoodVs = 0;
        double locMoodS = 0;
        double locMoodN = 0;
        double locMoodH = 0;
        double locMoodVh = 0;

        double locMoodTotalVs = 0;
        double locMoodTotalS = 0;
        double locMoodTotalN = 0;
        double locMoodTotalH = 0;
        double locMoodTotalVh = 0;

        double locMoodTotal = 0;


        for (int i = 0; i < userLocations.size(); i++) {
            int lId = database.getLocID(getLoggedInUser(), userLocations.get(i).getProvider());
            int[][] matrix = locMatrixBuilder(lId);
            for (int j = 0; j < 5; j++) {
                double rv = BayesHelper.locMoodCal(matrix, j, database.getVisitTime(lId, date));
                double rvTotal = BayesHelper.totalMatrixRow(matrix, 10, j);
                switch (j) {
                    case 0:
                        locMoodVs = locMoodVs + rv;
                        locMoodTotalVs = locMoodTotalVs + rvTotal;
                        break;
                    case 1:
                        locMoodS = locMoodS + rv;
                        locMoodTotalS = locMoodTotalS + rvTotal;
                        break;
                    case 2:
                        locMoodN = locMoodN + rv;
                        locMoodTotalN = locMoodTotalN + rvTotal;
                        break;
                    case 3:
                        locMoodH = locMoodH + rv;
                        locMoodTotalH = locMoodTotalH + rvTotal;
                        break;
                    case 4:
                        locMoodVh = locMoodVh + rv;
                        locMoodTotalVh = locMoodTotalVh + rv;
                        break;
                }

            }

            locMoodTotal = locMoodTotal + BayesHelper.totalMatrix(matrix, 10, 5);

        }

        LocMoodCalObject retval = new LocMoodCalObject(locMoodVs, locMoodS, locMoodN, locMoodH, locMoodVh,
                locMoodTotalVs, locMoodTotalS, locMoodTotalN, locMoodTotalH, locMoodTotalVh, locMoodTotal);
        return retval;
    }

    /**
     * Generates the matrices and variables needed to predict mood
     * Sends these values along with today's current values of steps, shakiness and screen on time to the prediction function.
     * @return - returns the prediction as a integer between 1 and 5
     */
    public int prediction() {
        int[][] matrixSteps = stepsMatrixBuilder();
        int[][] matrixShake = shakeMatrixBuilder();
        int[][] matrixOnTime = onTimeMAtrixBuilder();

        LocMoodCalObject locMoodCalObject = locMoodCalculation(getDate());

        System.out.println(locMoodCalObject.toString());


        int steps = Integer.parseInt(database.getSteps(date, getLoggedInUser()));
        int shakes = Integer.parseInt(database.getShake(getLoggedInUser(), date));
        int onTime = Integer.parseInt(database.getOnTime(getLoggedInUser(), date));

        return BayesHelper.predictMoodShakeOnTime(matrixSteps, matrixShake, matrixOnTime, shakes, steps, onTime,
                locMoodCalObject);


    }

    /**
     * Creates a notification channel for foreground service use.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

