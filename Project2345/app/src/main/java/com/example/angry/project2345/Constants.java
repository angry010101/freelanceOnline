package com.example.angry.project2345;

/**
 * Created by S1 on 2/8/2017.
 */
public class Constants {
    public static final String DBNAME = "ActiveCustomers";
    public static final int LOCATION_REFRESH_TIME = 4 * 1000; //ms
    public static final float LOCATION_REFRESH_DISTANCE = 10; //meters
    public static final int timerTime = 15 * 1000 * 60;
    public static final int alphatime = 15;
    public static final float alphak = (1f-0.4f)/alphatime;
    public static final float latlonamplitude = 5f;
    public static final float CameraMaxZoom = 17.0f;
    public static final float CameraMinZoom = 2.0f;
}
