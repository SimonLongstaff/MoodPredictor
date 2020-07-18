package com.example.moodpredictor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //Vars
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    double lattiude;
    double longitude;
    String title;

    //Constructor
    public MapFragment() {


    }

    //Set up
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.map_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText enterTitle = (EditText) Objects.requireNonNull(getView().findViewById(R.id.addTitle));

        ImageButton add =  Objects.requireNonNull(getView()).findViewById(R.id.addNewPlace);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = enterTitle.getText().toString();
                createSavedLocation();
            }
        });



        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mGoogleMap.clear();
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("New Marker");
                mGoogleMap.addMarker(marker);
                lattiude = latLng.latitude;
                longitude = latLng.longitude;
                System.out.println(lattiude + "---" + longitude);
            }
        });

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void createSavedLocation(){
        System.out.println("Title: " + title + "\nLongitude: " + longitude + "\n  Latitude: " + lattiude);
    }


}
