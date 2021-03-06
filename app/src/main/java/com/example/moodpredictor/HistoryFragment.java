package com.example.moodpredictor;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.fragment.app.Fragment;

public class HistoryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.history_layout,container,false);
        final MainActivity activity = (MainActivity) getActivity();


        final GraphView graphView = view.findViewById(R.id.graph);
        Button stepHistory = view.findViewById(R.id.ButtonStepsHistory);
        /**
         * Create the graph for step history
         */
        stepHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<StepMoodObject> stepsMoodList = activity.database.getStepsMoodDate(activity.database.getLoggedIn());
                DataPoint[] retval = new DataPoint[stepsMoodList.size()];
                for (int i =0; i<stepsMoodList.size();i++){
                    int steps = stepsMoodList.get(i).getSteps();
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = format.parse(stepsMoodList.get(i).getDate());
                    }catch (Exception e) {
                        e.printStackTrace();}
                    DataPoint rv = new DataPoint(date,steps);
                    retval[i] = rv;
                }
                LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>(retval);
                graphView.removeAllSeries();
                graphView.setTitle("Step History");
                graphView.addSeries(graphSeries);
                graphView.getViewport().setScalable(true);
                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMaxY(15000);
                graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(activity));
            }
        });

        final Button shakeHistory = view.findViewById(R.id.ButtonShakeHistory);
        /**
         * Creates the graph for shake history
         */
        shakeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ShakeMoodObject> shakeMood = activity.database.getShakeMoodDate(activity.database.getLoggedIn());
                DataPoint[] retval = new DataPoint[shakeMood.size()];
                for (int i =0; i<shakeMood.size();i++){
                    int shakes = shakeMood.get(i).getShakes();
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = format.parse(shakeMood.get(i).getDate());
                    }catch (Exception e) {
                        e.printStackTrace();}
                    DataPoint rv = new DataPoint(date,shakes);
                    retval[i] = rv;
                }
                LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>(retval);
                graphView.removeAllSeries();
                graphView.setTitle("Shake History");
                graphView.addSeries(graphSeries);
                graphView.getViewport().setScalable(true);
                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMaxY(15000);
                graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(activity));
            }
        });

        Button moodHistory = view.findViewById(R.id.ButtonMoodHistory);
        /**
         * creates the graph for mood history
         */
        moodHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<StepMoodObject> stepsMoodList = activity.database.getStepsMoodDate(activity.database.getLoggedIn());
                DataPoint[] retval = new DataPoint[stepsMoodList.size()];
                for (int i =0; i<stepsMoodList.size();i++){
                    int mood = stepsMoodList.get(i).getMood();
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = format.parse(stepsMoodList.get(i).getDate());
                    }catch (Exception e) {
                        e.printStackTrace();}
                    DataPoint rv = new DataPoint(date,mood);
                    retval[i] = rv;
                }
                LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>(retval);
                graphView.removeAllSeries();
                graphView.getViewport().setYAxisBoundsManual(false);
                graphView.setTitle("Mood History");
                graphView.addSeries(graphSeries);
                graphView.getViewport().setScalable(true);
                graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(activity));
            }
        });


        return view;
    }

}
