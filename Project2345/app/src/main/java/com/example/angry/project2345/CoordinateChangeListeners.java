package com.example.angry.project2345;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.androidmapsextensions.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by S1 on 3/1/2017.
 */
public class CoordinateChangeListeners {
    public static HashMap<String,Boolean> addedkeys = new HashMap<>();
    private static final String TAG = "CoordinateChangel";
    private static final double amp = Constants.latlonamplitude;

    public static ValueEventListener coordChangeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            handlefunction(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    public static void handlefunction(DataSnapshot dataSnapshot){
      //  double lat= MapsActivity.getLat(),lon = MapsActivity.getLon();
        String key = DbFirebase.key;
        Log.d(TAG, "handlefunction: started");
        addedkeys = new HashMap<>();
        MapsActivity.getmMap().clear();
        if (MarkerTools.mClusterManager != null) MarkerTools.mClusterManager.clearItems();
        if (dataSnapshot == null) {
            Log.d(TAG, "handlefunction: returned datasnapshot null");
            return;
        }
        Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.child(Constants.DBNAME).getValue();

        if (td == null) {
            Log.d(TAG, "handlefunction: returned null");
            return;
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                td.get(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null){
            td.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        Iterator<String> iter = td.keySet().iterator();
        String key1="";
        UserInfoClass.TaskInfo asideusr;
        while(iter.hasNext()) {
            key1 = iter.next();
            Log.d(TAG, "handlefunction: add key" + key1);
            asideusr = dataSnapshot.child(Constants.DBNAME).child(key1).getValue(UserInfoClass.TaskInfo.class);
            if(new Date().getTime() - asideusr.getDate().getTime()
                    > Constants.alphatime * 1000 * 60){
                DbFirebase.removeTask(asideusr.getuId());
                Log.d(TAG, "onDataChange: remove task due to date");
                continue;
            }
        /*    if (asideusr.getLat()<lat+amp && asideusr.getLat()>lat-amp){
                if (asideusr.getLon()<lon+amp && asideusr.getLon()>lon-amp){
                   */ if (addedkeys.get(key1) == null) {
                        Log.d(TAG, "handlefunction: add marker coordchangelistener");
                        MarkerTools.addMarker(asideusr,key1);
                        addedkeys.put(key1,true);
                    }/*
                } else continue;
            }
            else continue;*/

            // MapsActivity.setNewUserMarker(asideusr);
          //
            //
            // MarkerTools.addMarker(asideusr,key1);

        }
    }

    public static GoogleMap.OnCameraChangeListener cameraChangeListener = new com.androidmapsextensions.GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            float maxZoom = Constants.CameraMaxZoom;
            float minZoom = Constants.CameraMinZoom;

            if (cameraPosition.zoom > maxZoom) {
                MapsActivity.getmMap().animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
            } else if (cameraPosition.zoom < minZoom) {
                MapsActivity.getmMap().animateCamera(CameraUpdateFactory.zoomTo(minZoom));
            }
        }
    };

    public static LocationListener mLocationListener = new LocationListener() {

        @Override
        public synchronized void onLocationChanged(Location location) {
            //code
            /*
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));*/
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            Log.d(TAG, "onLocationChanged: ");
            MapsActivity.mylocation = location;
            DbFirebase.updateMyDate();
            DbFirebase.updateDbMyLocation(location);
            notify();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
