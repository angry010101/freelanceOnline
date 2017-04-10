package com.example.angry.project2345;

import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by S1 on 2/9/2017.
 */
public class DbFirebase {
    private static final String TAG = "testdb";
    private static FirebaseDatabase database;
    private static DatabaseReference myRef,mynewRef;
    public static String key="";
    private static FirebaseUser user;
    private static boolean setUpded=false;


    public static boolean setup(){
        if (setUpded) return true;
        key="";
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        if (myRef == null) {
            return false;
        }
        user = DbUserData.user;
        setUpded = true;
        return true;
        /*
        addMyPositionInfo(userInfo);
        setUpdateinRealtime();
        getActiveCustomers();
        */
    }



    public static void addTaskInfo(final String name, final String desc, final String curr){
        Thread t = new Thread(new Runnable() {
            public void run() {

                UserInfoClass.TaskInfo taskInfo = new UserInfoClass.TaskInfo();
                taskInfo.setName(name);
                taskInfo.setDescription(desc);
                taskInfo.setReward(curr);
                taskInfo.setDate(new Date());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                taskInfo.setuId(user.getUid());
                taskInfo.setLon(MapsActivity.getLon());
                taskInfo.setLat(MapsActivity.getLat());
                myRef.child(Constants.DBNAME).child(user.getUid())
                        .setValue(taskInfo);

            }
        });
        t.start();

    }
    private static String mPhoneNumber="";
    public static void addMyPositionInfo(){
        UserInfoClass.TaskInfo t = new UserInfoClass.TaskInfo();

        HashMap<String,Object> m = new HashMap<String, Object>();
        m.put("lat",MapsActivity.mylocation.getLatitude());
        m.put("lon",MapsActivity.mylocation.getLongitude());
        m.put("date",new Date());

        myRef.child(Constants.DBNAME).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(m);
     /*   myRef.child("users").child(user.getUid()).setValue();
                userInfo.setDate(new Date());
                if (key.equals("")) key = mynewRef.getKey();
                mynewRef.setValue(userInfo);*/

    }


    public static void createNewUser(){
        FirebaseUser user = DbUserData.user;
        UserInfoClass.UserInfo u = new UserInfoClass.UserInfo();
        u.setDate(new Date());
        u.setNegativemarks(0);
        u.setPositivemarks(0);
        String e = user.getEmail();
        u.setNumber(e.substring(0,e.indexOf("@")));
        DbFirebase.setup();
        myRef.child("users").child(user.getUid()).setValue(u);
    }

    private static void setUpdateinRealtime(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
              //  UserInfoClass.UserInfo value = dataSnapshot.child(key).getValue(UserInfoClass.UserInfo.class);
              //  if (value == null) return;
                MapsActivity.getmMap().clear();
                CoordinateChangeListeners.addedkeys.clear();
                Log.d(TAG, "onDataChange: MAP has been cleaned");
                MapsActivity.upDateUsers();
                CoordinateChangeListeners.handlefunction(dataSnapshot);
                //getActiveCustomers();

                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
              //  Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void writeNewUser(String userId, String name, String email) {
        UserInfoClass.UserInfo user = new UserInfoClass.UserInfo();
        myRef.child("users").child(userId).setValue(user);
    }

    public static void removeBykey(String key){
        if (key != null && myRef!=null) myRef.child(key).removeValue();
        Log.d(TAG, "removeBykey: removed");
    }

    public static void removeTask(String uid){
        if (myRef != null){
            myRef.child(Constants.DBNAME)
                    .child(uid).removeValue();
            Log.d(TAG, "removeTask: task removed");
        }

    }

    public static void updateMyDate(){
        if (myRef == null) return;
        HashMap<String,Object> map = new HashMap<String, Object>() ;
        map.put("date", new Date());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null)return;
        myRef.child(Constants.DBNAME).child(user.getUid()).updateChildren(map);
    }
    public static void getActiveCustomers(){
       // if (myRef == null) return;
        float amplitude = Constants.latlonamplitude;
       /* double lat1= MapsActivity.getLat(),
                lon1 = MapsActivity.getLon();
*/
        myRef.addValueEventListener(CoordinateChangeListeners.coordChangeListener);
      /*  myRef.orderByChild("lat").startAt(lat1-amplitude)
                .endAt(lat1+amplitude)./*
                orderByChild("lon").
                startAt(lon1-amplitude).
                endAt(lon1+amplitude).// + order by lon
                addValueEventListener(CoordinateChangeListeners.coordChangeListener);

        myRef.orderByChild("lon").startAt(lon1-amplitude)
                .endAt(lat1+amplitude).addValueEventListener(CoordinateChangeListeners.coordChangeListener);*/
    }



    public static void updateDbMyLocation(Location location){
        if (key.equals("")) return;
        HashMap<String,Object> map = new HashMap<String, Object>() ;
        map.put("lat", location.getLatitude());
        map.put("lon", location.getLongitude());
        myRef.child("users").child(user.getUid()).updateChildren(map);
    }


    public static void getInfo(){

    }

    public static ArrayList<UserInfoClass.TaskInfo> getTasksByKey(String key){
        ArrayList<UserInfoClass.TaskInfo> arr = new ArrayList<UserInfoClass.TaskInfo>();
        return arr;
    }

    private static boolean listenerenabled = false;
    private static String keyselectedtask,selectedtask;
    private static UserInfoClass.TaskInfo info_user_data = null;


    public static UserInfoClass.TaskInfo getInfoUserData(){
        return info_user_data;
    }
    public static void setTaskListener(final String key1, final String task){
        keyselectedtask = key1;
        selectedtask = task;
        if (myRef != null) {
            myRef.addListenerForSingleValueEvent(getTaskListener);
        }
    }

    public static void getMyTasks(){
        if (myRef == null) return;
        myRef.addListenerForSingleValueEvent(myTaskViewListener);
    }

    private static ValueEventListener myTaskViewListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot == null) return;
            if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
            UserInfoClass.TaskInfo t = dataSnapshot.child(Constants.DBNAME).child(
                    FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(UserInfoClass.TaskInfo.class);
            if (t == null){
                Log.d(TAG, "onDataChange: t == null");
            }
            Handler h = MyTasksActivity.h;
            h.sendMessage(h.obtainMessage(1,t));
            myRef.removeEventListener(myTaskViewListener);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private static ValueEventListener getTaskListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            UserInfoClass.TaskInfo t = dataSnapshot.child(keyselectedtask).
                    child(selectedtask).getValue(UserInfoClass.TaskInfo.class);
            info_user_data = t;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private static String keyselectedinfotask="",selectedinfotask="";
    private static ValueEventListener getTaskInfoListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot == null) return;
            if (keyselectedinfotask == null || selectedinfotask == null) return;
            UserInfoClass.TaskInfo t = dataSnapshot.child(Constants.DBNAME).child(keyselectedinfotask)
                    .getValue(UserInfoClass.TaskInfo.class);
            Handler h = MarkerTools.h;
            h.sendMessage(h.obtainMessage(1,t));
            Log.d(TAG, "onDataChange: Value obtained");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public static void loadDataTaskView(String key2, String selectedTask) {
        keyselectedinfotask=key2;
        selectedinfotask = selectedTask;
        Log.d(TAG, "loadDataTaskView: "  +keyselectedinfotask + " " + selectedinfotask);
        myRef.addListenerForSingleValueEvent(getTaskInfoListener);
    }

    public static void removeTaskInfoListener(){
        myRef.removeEventListener(getTaskInfoListener);
    }

    public static void updatePhone(String newphone){
        HashMap<String,Object> map = new HashMap<String, Object>() ;
        map.put("number", newphone);
        if (key != null) myRef.child(key).updateChildren(map);
    }
}
