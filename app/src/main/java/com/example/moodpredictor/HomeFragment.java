package com.example.moodpredictor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_layout, container, false);
        final MainActivity activity = (MainActivity) getActivity();

        if (!activity.database.shakeExists(activity.getLoggedInUser(), activity.getDate())) {
            activity.database.newShake(activity.getLoggedInUser(), activity.getDate(), 0);
        }

        if (!activity.database.onTimeExists(activity.getLoggedInUser(), activity.getDate())) {
            activity.database.newOntime(activity.getLoggedInUser(), activity.getDate(), 0);
        }

        if (activity.database.stepIDExists(activity.getDate(), activity.getLoggedInUser())) {
            activity.database.insertNewStepDay(0, activity.getLoggedInUser(), activity.getDate());
        }


        final TextView stepnum = view.findViewById(R.id.numSteps);
        stepnum.setText(activity.getSteps());

        final TextView shakenum = view.findViewById(R.id.numShake);
        shakenum.setText(JitterString());

        final TextView mood = view.findViewById(R.id.TextMoodPredict);
        int result = activity.prediction();
        switch (result) {
            case (0):
                mood.setText("Not enough Data");
                break;
            case (1):
                mood.setText("Very Poor");
                mood.setTextColor(getResources().getColor(R.color.red));
                break;
            case (2):
                mood.setText("Poor");
                mood.setTextColor(getResources().getColor(R.color.orange));
                break;
            case (3):
                mood.setText("Neutral");
                mood.setTextColor(getResources().getColor(R.color.grey));
                break;
            case (4):
                mood.setText("Good");
                mood.setTextColor(getResources().getColor(R.color.lime));
                break;
            case (5):
                mood.setText("Very Good");
                mood.setTextColor(getResources().getColor(R.color.green));
                break;

        }

        final Chronometer chronometer = view.findViewById(R.id.timer);
        System.out.println("Time: " + activity.database.getOnTime(activity.getLoggedInUser(), activity.getDate()));
        chronometer.setBase(SystemClock.elapsedRealtime() - ((Long.parseLong(activity.database.getOnTime(activity.getLoggedInUser(), activity.getDate())) * 1000)));
        chronometer.start();


        FloatingActionButton refresh = (FloatingActionButton) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.updateAreas();
                stepnum.setText(activity.getSteps());
                shakenum.setText(JitterString());
                chronometer.setBase(SystemClock.elapsedRealtime() - ((Long.parseLong(activity.database.getOnTime(activity.getLoggedInUser(), activity.getDate())) * 1000)));
                chronometer.start();
                int result = activity.prediction();
                switch (result) {
                    case (1):
                        mood.setText("Very Poor");
                        mood.setTextColor(getResources().getColor(R.color.red));
                        break;
                    case (2):
                        mood.setText("Poor");
                        mood.setTextColor(getResources().getColor(R.color.orange));
                        break;
                    case (3):
                        mood.setText("Neutral");
                        mood.setTextColor(getResources().getColor(R.color.grey));
                        break;
                    case (4):
                        mood.setText("Good");
                        mood.setTextColor(getResources().getColor(R.color.lime));
                        break;
                    case (5):
                        mood.setText("Very Good");
                        mood.setTextColor(getResources().getColor(R.color.green));
                        break;

                }


            }
        });


        Button openMoodPopup = view.findViewById(R.id.set_mood);
        openMoodPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        return view;
    }

    public void showPopup(View view) {

        View popupView = getLayoutInflater().inflate(R.layout.moodpopup, null);
        final MainActivity activity = (MainActivity) getActivity();

        final String date = activity.getDate();

        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        TextView close = popupView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        //Very Bad button
        LinearLayout veryBad = popupView.findViewById(R.id.mood_very_bad);
        veryBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(activity.getLoggedInUser(), date)) {
                    activity.database.updateMood(activity.getLoggedInUser(), date, 1);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 1, date);
                Toast.makeText(getContext(),"Mood Update",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

            }
        });

        // Bad button
        LinearLayout bad = popupView.findViewById(R.id.mood_bad);
        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(activity.getLoggedInUser(), date)) {
                    activity.database.updateMood(activity.getLoggedInUser(), date, 2);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 2, date);
                Toast.makeText(getContext(),"Mood Update",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

            }
        });

        //Neutral button
        LinearLayout neutral = popupView.findViewById(R.id.mood_neutral);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(activity.getLoggedInUser(), date)) {
                    activity.database.updateMood(activity.getLoggedInUser(), date, 3);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 3, date);
                Toast.makeText(getContext(),"Mood Update",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

            }
        });

        //Good button
        LinearLayout good = popupView.findViewById(R.id.mood_good);
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(activity.getLoggedInUser(), date)) {
                    activity.database.updateMood(activity.getLoggedInUser(), date, 4);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 4, date);
                Toast.makeText(getContext(),"Mood Update",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

            }
        });

        //Very Good button
        LinearLayout veryGood = popupView.findViewById(R.id.mood_very_good);
        veryGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(activity.getLoggedInUser(), date)) {
                    activity.database.updateMood(activity.getLoggedInUser(), date, 5);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 5, date);
                Toast.makeText(getContext(),"Mood Update",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();


            }
        });


        popupWindow.setFocusable(true);

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, view.getHeight());

    }

    @Override
    public void onPause() {
        super.onPause();
        Chronometer chronometer = getView().findViewById(R.id.timer);
        chronometer.stop();
    }

    @Override
    public void onStart() {
        super.onStart();
        final MainActivity activity = (MainActivity) getActivity();
        Chronometer chronometer = getView().findViewById(R.id.timer);
        chronometer.start();
    }


    public String JitterString() {
        MainActivity mainActivity = (MainActivity) getActivity();
        int shakebucket = mainActivity.getShakeBucketed();
        String retval;

        switch (shakebucket) {
            case (0):
                retval = "Very Low";
                break;
            case (1):
                retval = "Low";
                break;
            case (2):
                retval = "Moderate";
                break;
            case (3):
                retval = "High";
                break;
            case (4):
                retval = "Very High";
                break;
            case (5):
                retval = "Extremly High";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + shakebucket);
        }
        return retval;
    }


}
