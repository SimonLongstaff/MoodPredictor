package com.example.moodpredictor;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);

        MainActivity activity = (MainActivity) getActivity();
        TextView stepnum = view.findViewById(R.id.numSteps);
        stepnum.setText(activity.getSteps());

        TextView shakenum = view.findViewById(R.id.numShake);
        shakenum.setText(activity.getShake());

        int[][] matrixSteps = matrixBuilder();
        int[][] matrixShake = shakeMatrixBuilder();
        BayesHelper.predictMoodSteps(matrixSteps, 3600);
        BayesHelper.predictMoodShake(matrixShake, 3000);
        BayesHelper.predictMoodShakeStep(matrixSteps,matrixShake,5000,3000);


        Button openMoodPopup = (Button) view.findViewById(R.id.set_mood);
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
        final int user = activity.getLoggedInUser();
        final String mId = user + date;

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
                if (activity.database.moodIDExists(activity.getLoggedInUser(),date)) {
                    activity.database.updateMood(activity.getLoggedInUser(),date, 1);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 1, date);
                popupWindow.dismiss();

            }
        });

        // Bad button
        LinearLayout bad = popupView.findViewById(R.id.mood_bad);
        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(activity.getLoggedInUser(),date)) {
                    activity.database.updateMood(activity.getLoggedInUser(),date, 2);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 2, date);
                popupWindow.dismiss();

            }
        });

        //Neutral button
        LinearLayout neutral = popupView.findViewById(R.id.mood_neutral);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(activity.getLoggedInUser(),date)) {
                    activity.database.updateMood(activity.getLoggedInUser(),date, 3);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 3, date);
                popupWindow.dismiss();

            }
        });

        //Good button
        LinearLayout good = popupView.findViewById(R.id.mood_good);
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(activity.getLoggedInUser(),date)) {
                    activity.database.updateMood(activity.getLoggedInUser(),date, 4);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 4, date);
                popupWindow.dismiss();

            }
        });

        //Very Good button
        LinearLayout veryGood = popupView.findViewById(R.id.mood_very_good);
        veryGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(activity.getLoggedInUser(),date)) {
                    activity.database.updateMood(activity.getLoggedInUser(),date, 5);
                } else
                    activity.database.newMood(activity.getLoggedInUser(), 5, date);
                popupWindow.dismiss();


            }
        });


        popupWindow.setFocusable(true);

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, view.getHeight());

    }

    //Build the step matrix for Bayesian prediction
    public int[][] matrixBuilder() {
        final MainActivity activity = (MainActivity) getActivity();

        ArrayList<StepMoodObject> matrixBuilder = activity.database.getStepsMood(activity.getLoggedInUser());
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

    public int[][] shakeMatrixBuilder(){
        final MainActivity activity = (MainActivity) getActivity();
        ArrayList<ShakeMoodObject> matrixBuilder = activity.database.getShakeMood(activity.getLoggedInUser());

        int[][] matrix = {{1,1,1,1,1,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            //System.out.println(i);
            int shakes = matrixBuilder.get(i).getShakes();
            int mood = matrixBuilder.get(i).getMood() - 1;
            int shakeBucket = BayesHelper.shakeBucket(shakes);


            matrix[mood][shakeBucket]++;
        }
        System.out.println("Shake: " + Arrays.deepToString(matrix));
        return matrix;

    }



}
