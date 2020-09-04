package com.example.moodpredictor;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LocationRatingFragment extends Fragment {

    int highest = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.location_rating_layout, container, false);

        final ArrayList<String> userLocations = activity.getUserLocations();
        final int[] i = {0};

        final GraphView graphView = view.findViewById(R.id.graph);
        Button next = view.findViewById(R.id.ButtonNext);
        Button prev = view.findViewById(R.id.ButtonPrev);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(5.5);

        /**
         * Cycles through the users locations
         */
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i[0]++;
                if (i[0] == userLocations.size()){
                    i[0] = 0;
                }

                graphView.removeAllSeries();
                graphView.addSeries(updateGraph(activity.database.getLocID(1,userLocations.get(i[0]))));
                graphView.getViewport().setMaxY(highest+1);
                graphView.setTitle(userLocations.get(i[0]));
            }
        });

        /**
         * Cycles through the user locations in reverse
         */
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i[0] <= 0){
                    i[0] = userLocations.size()-1;
                }
                else  i[0]--;

                graphView.removeAllSeries();
                graphView.addSeries(updateGraph(activity.database.getLocID(1,userLocations.get(i[0]))));
                graphView.getViewport().setMaxY(highest+1);
                graphView.setTitle(userLocations.get(i[0]));
            }
        });



        return view;
    }

    /**
     * Function to update the graph for location mood history
     * @param lID - location ID
     * @return a graphview bargraph series for the Location ID it was given
     */
    public BarGraphSeries updateGraph(int lID){

        MainActivity activity = (MainActivity) getActivity();

        int vs = 0;
        int s = 0;
        int n = 0;
        int h = 0;
        int vh =0;

        ArrayList<LocationMoodObject> locmood = activity.database.getLocMood(lID);
        for (int i = 0; i<locmood.size();i++){
            switch (locmood.get(i).mood){
                case (1):
                    vs = vs+1;
                    break;
                case(2):
                    s = s+1;
                    break;
                case(3):
                    n = n+1;
                    break;
                case(4):
                    h = h+1;
                    break;
                case(5):
                    vh = vh +1;
                    break;
            }
        }

        int[] numbers = {vs,s,n,h,vh};
        higestValue(numbers);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1, vs),
                new DataPoint(2,s),
                new DataPoint(3,n),
                new DataPoint(4,h),
                new DataPoint(5,vh)
        });
        series.setSpacing(20);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });
        return series;
    }

    /**
     * Sets the highest value in an array to a global variable
     * @param numbers - array to be analysed
     */
    public void higestValue(int[] numbers){
        int maxValue = numbers[0];
        for(int i=1;i < numbers.length;i++){
            if(numbers[i] > maxValue){
                maxValue = numbers[i];
            }
        }
        highest = maxValue;

    }
}
