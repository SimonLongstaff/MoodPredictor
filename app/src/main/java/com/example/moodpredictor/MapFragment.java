package com.example.moodpredictor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
    MainActivity mainActivity = (MainActivity) getActivity();
    double lattiude;
    double longitude;
    LatLng CirclelatLng;
    String title;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private static final String TAG = "MapFragment";

    //Geofence
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;


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
        geofenceHelper = new GeofenceHelper(getContext());
        geofencingClient = LocationServices.getGeofencingClient(getActivity());
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
                addGeofence(CirclelatLng, title);
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
                CirclelatLng = latLng;

                System.out.println(lattiude + "---" + longitude);
            }
        });

        enableUserLocation();
        enableBackgroundLocation();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        }

    }


    private void enableBackgroundLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Background Location", Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(getContext(), "Background fail", Toast.LENGTH_LONG).show();
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
        System.out.println("Title: " + title + "\nLongitude: " + longitude + "\n  Latitude: " + lattiude);
        addCircle(CirclelatLng, 100);

    }

    @SuppressLint("MissingPermission")
    private void addGeofence(LatLng latLng, String title) {

        Geofence geofence = geofenceHelper.getGeofence(title, latLng, 100, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT );
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        GeofencingRequest geofencingRequest = geofenceHelper.geofencingRequest(geofence);

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence added");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMsg = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMsg);
                    }
                });
    }


}


