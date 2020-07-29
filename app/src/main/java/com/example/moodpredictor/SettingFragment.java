package com.example.moodpredictor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        TextView sUser = view.findViewById(R.id.Setting_User);
        final MainActivity activity = (MainActivity) getActivity();
        sUser.setText(activity.getLoggedInName());

        Button debugSteps = view.findViewById(R.id.DEBUG_addsteps);
        debugSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newSteps = Integer.parseInt(activity.database.getSteps(activity.date,activity.getLoggedInUser())) + 1000;
                activity.database.updateSteps(activity.date,activity.getLoggedInUser(),newSteps);
            }
        });

        Button debugShakes = view.findViewById(R.id.DEBUG_addShakes);
        debugShakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newShakes = Integer.parseInt(activity.database.getShake(activity.getLoggedInUser(),activity.date)) + 1000;
                activity.database.updateShake(activity.getLoggedInUser(),activity.date,newShakes);
            }
        });

        Button debugReSteps = view.findViewById(R.id.DEBUG_removesteps);
        Button debugReShake = view.findViewById(R.id.DEBUG_removeShakes);

        debugReSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newSteps = Integer.parseInt(activity.database.getSteps(activity.date,activity.getLoggedInUser())) - 1000;
                activity.database.updateSteps(activity.date,activity.getLoggedInUser(),newSteps);
            }
        });

        debugReShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newShakes = Integer.parseInt(activity.database.getShake(activity.getLoggedInUser(),activity.date)) - 1000;
                activity.database.updateShake(activity.getLoggedInUser(),activity.date,newShakes);
            }
        });

        return view;

    }




}
