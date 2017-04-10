package com.example.angry.project2345;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by angry on 26.03.2017.
 */
public class MapsActivityStart {
    public static void initForAuthUsrMap(Context context, FragmentActivity activity){
    }

    public static boolean askForPermissions(Context context){

        LocationManager mLocationManager = MapsActivity.mLocationManager;
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return false;
            else {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.LOCATION_REFRESH_TIME,
                        Constants.LOCATION_REFRESH_DISTANCE, CoordinateChangeListeners.mLocationListener);
            }
        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.LOCATION_REFRESH_TIME,
                    Constants.LOCATION_REFRESH_DISTANCE, CoordinateChangeListeners.mLocationListener);
        }

        GoogleMap mMap = MapsActivity.getmMap();
        mMap.setMyLocationEnabled(true);
        //  mMap.setInfoWindowAdapter(MarkerTools.infoWindowAdapter);

        MapsActivity.mylocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (MapsActivity.mylocation == null){
            Log.d("loc", "onMapReady: NOT USED GPS");
            MapsActivity.mylocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (MapsActivity.mylocation == null) {
                Log.d("loc", "onMapReady: LOCATION == null");
            }
            else{
                Log.d("loc", "onMapReady: USED NETWORK");
            }
        }
        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(
                    new LatLng(
                            MapsActivity.mylocation.getLatitude(),
                            MapsActivity.mylocation.getLongitude()
                    )));
            DbFirebase.addMyPositionInfo();
        }

        return true;

    }

    public static void sendDataToDB(){

    }
    public static void s(){}
}
