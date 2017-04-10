package com.example.angry.project2345;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by S1 on 3/7/2017.
 */
public class GetMyInfo{
    public static double lon,lat;
    public static String num;
    static ExecutorService exec = Executors.newFixedThreadPool(5);
    public static UserInfoClass.UserInfo get() throws ExecutionException, InterruptedException {
        UserInfoClass.UserInfo usr = new UserInfoClass.UserInfo();

        ArrayList<Double> cd = getCoords();
        String ph=getPhone();

        usr.setDate(new Date());
        usr.setNumber(ph);

        Log.d("getusrinfo", "get: " + ph + " " + cd.get(0) + " " + cd.get(1));

        exec.shutdown();
        System.out.println("end");

        return usr;
    }

    public static String getPhone() throws ExecutionException, InterruptedException {
        ExecutorService e = Executors.newSingleThreadExecutor();
        ArrayList<Future<String>> numarr = new ArrayList<>();
        numarr.add(e.submit(new getNum()));
        String s = numarr.get(0).get();
        e.shutdown();
        return s;
    }

    public static ArrayList<Double> getCoords() throws ExecutionException, InterruptedException {
        ExecutorService e = Executors.newSingleThreadExecutor();
        ArrayList<Future<ArrayList<Double>>> coordarr = new ArrayList<>();
        coordarr.add(exec.submit(new getCoord()));
        ArrayList<Double> cd = coordarr.get(0).get();
        e.shutdown();
        return cd;
    }

    public static class getCoord implements Callable<ArrayList<Double>> {
        @Override
        public synchronized ArrayList<Double> call() throws Exception {
            ArrayList<Double> arr = new ArrayList<>();
            while (MapsActivity.getLat() == null || MapsActivity.getLon() == null)
                wait();
            arr.add(MapsActivity.getLat());
            arr.add(MapsActivity.getLon());
            return arr;
        }
    }

    public static class getNum implements Callable<String> {
        @Override
        public synchronized String call() throws Exception {
            TelephonyManager tMgr = (TelephonyManager) MapsActivity.getContext().
                    getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            if (mPhoneNumber == null || mPhoneNumber.equals("")){
                //show dialog set phone number
                /*Context context = MapsActivity.getContext();
                context.startActivity(new Intent(context, Settings_act.class));
                */
                return "";
            }
            return mPhoneNumber;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
}
