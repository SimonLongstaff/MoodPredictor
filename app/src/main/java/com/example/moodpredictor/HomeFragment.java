package com.example.moodpredictor;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import androidx.fragment.app.Fragment;

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_layout, container, false);

    }

    public void setSteps(String s) {
        TextView Stepnum = Objects.requireNonNull(getView()).findViewById(R.id.numSteps);
        Stepnum.setText(s);

    }

}
