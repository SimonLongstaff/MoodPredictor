package com.example.moodpredictor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

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
        stepHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<StepMoodObject> stepsMoodList = activity.database.getStepsMood(activity.getLoggedInUser());
                DataPoint[] retval = new DataPoint[stepsMoodList.size()];
                for (int i =0; i<stepsMoodList.size();i++){
                    int steps = stepsMoodList.get(i).getSteps();
                    int id = stepsMoodList.get(i).getUid();
                    DataPoint rv = new DataPoint(id,steps);
                    retval[i] = rv;
                }
                LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>(retval);
                graphView.removeAllSeries();
                graphView.setTitle("Step History");
                graphView.addSeries(graphSeries);
            }
        });

        final Button shakeHistory = view.findViewById(R.id.ButtonShakeHistory);
        shakeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ShakeMoodObject> shakeMood = activity.database.getShakeMood(activity.getLoggedInUser());
                DataPoint[] retval = new DataPoint[shakeMood.size()];
                for (int i =0; i<shakeMood.size();i++){
                    int shakes = shakeMood.get(i).getShakes();
                    int id = shakeMood.get(i).getUid();
                    DataPoint rv = new DataPoint(id,shakes);
                    retval[i] = rv;
                }
                LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>(retval);
                graphView.removeAllSeries();
                graphView.setTitle("Shake History");
                graphView.addSeries(graphSeries);
            }
        });

        Button moodHistory = view.findViewById(R.id.ButtonMoodHistory);
        moodHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<StepMoodObject> stepsMoodList = activity.database.getStepsMood(activity.getLoggedInUser());
                DataPoint[] retval = new DataPoint[stepsMoodList.size()];
                for (int i =0; i<stepsMoodList.size();i++){
                    int mood = stepsMoodList.get(i).getMood();
                    int id = stepsMoodList.get(i).getUid();
                    DataPoint rv = new DataPoint(id,mood);
                    retval[i] = rv;
                }
                LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>(retval);
                graphView.removeAllSeries();
                graphView.setTitle("Mood History");
                graphView.addSeries(graphSeries);
            }
        });



        return view;
    }

}
