package com.example.moodpredictor;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditLocationFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final MainActivity mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.edit_location,container,false);

        final ListView listView = view.findViewById(R.id.List_location);
        ArrayList<String> locations = mainActivity.getUserLocations();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, locations);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object select = listView.getItemAtPosition(i);
                int lID = mainActivity.database.getLocID(mainActivity.getLoggedInUser(),select.toString());
                mainActivity.database.deleteLoc(lID);
                mainActivity.updateAreas();
                ArrayList<String> locations = mainActivity.getUserLocations();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, locations);
                listView.setAdapter(arrayAdapter);

            }
        });

        return view;
    }
}
