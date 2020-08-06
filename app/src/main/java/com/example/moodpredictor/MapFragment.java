package com.example.moodpredictor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //Vars
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    double lattiude;
    double longitude;
    LatLng CirclelatLng;
    String title;
    private static final String TAG = "MapFragment";

    //Constructor
    public MapFragment() {    }

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

        ImageButton add = Objects.requireNonNull(getView()).findViewById(R.id.addNewPlace);
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
                addLocationsCircle();
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("New Marker");
                mGoogleMap.addMarker(marker);
                lattiude = latLng.latitude;
                longitude = latLng.longitude;
                CirclelatLng = latLng;

                System.out.println(lattiude + "---" + longitude);
            }
        });

        enableUserLocation();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        addLocationsCircle();
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        } else {
            int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
    }


    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(64, 255, 0, 0));
        circleOptions.strokeWidth(4);
        mGoogleMap.addCircle(circleOptions);
    }


    public void createSavedLocation() {
        MainActivity activity = (MainActivity) getActivity();
        System.out.println("Title: " + title + "\nLongitude: " + longitude + "\n  Latitude: " + lattiude);
        addCircle(CirclelatLng, 100);
        activity.database.newLocation(activity.getLoggedInUser(),title,longitude,lattiude);
        activity.updateAreas();

    }

    public void addLocationsCircle(){
        MainActivity activity = (MainActivity) getActivity();
        ArrayList<LatLng> locations = activity.getUserLocLatLan();

        for (int i = 0; i<locations.size();i++){

            addCircle(locations.get(i),100);
        }
    }
}


