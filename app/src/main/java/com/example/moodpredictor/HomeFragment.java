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
        TextView Stepnum = view.findViewById(R.id.numSteps);
        Stepnum.setText(activity.getSteps());

        Button openMoodPopup = (Button)view.findViewById(R.id.set_mood);
        openMoodPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        return view;


    }

    public void showPopup(View view){

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
               if (activity.database.moodIDExists(mId)){
                   activity.database.updateMood(mId, 1);
               }

               else
                   activity.database.newMood(mId,user,1,date);
                popupWindow.dismiss();

            }
        });

        // Bad button
        LinearLayout bad = popupView.findViewById(R.id.mood_bad);
        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(mId)){
                    activity.database.updateMood(mId, 2);
                }

                else
                    activity.database.newMood(mId,user,2,date);
                popupWindow.dismiss();

            }
        });

        //Neutral button
        LinearLayout neutral = popupView.findViewById(R.id.mood_neutral);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(mId)){
                    activity.database.updateMood(mId, 3);
                }

                else
                    activity.database.newMood(mId,user,3,date);
                popupWindow.dismiss();

            }
        });

        //Good button
        LinearLayout good = popupView.findViewById(R.id.mood_good);
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(mId)){
                    activity.database.updateMood(mId, 4);
                }

                else
                    activity.database.newMood(mId,user,4,date);
                popupWindow.dismiss();

            }
        });

        //Very Good button
        LinearLayout veryGood = popupView.findViewById(R.id.mood_very_good);
        veryGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.database.moodIDExists(mId)){
                    activity.database.updateMood(mId, 5);
                }

                else
                    activity.database.newMood(mId,user,5,date);
                popupWindow.dismiss();


            }
        });



        popupWindow.setFocusable(true);

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, view.getHeight());
        
    }


}
