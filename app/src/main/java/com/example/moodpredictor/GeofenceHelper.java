package com.example.moodpredictor;

import android.util.Log;

import com.google.android.gms.location.Geofence;

public class GeofenceHelper {


  public Geofence createGeoFence(String name, double lon, double lat){
    Log.d("tag","createGeofence");
    return new Geofence.Builder()
            .setRequestId(name)
            .setCircularRegion(lat, lon, 100)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_EXIT)
            .build();


  }
}