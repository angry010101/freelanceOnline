package com.example.angry.project2345;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFloat;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.concurrent.ExecutionException;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = "Location";
    private static GoogleMap mMap;


    private ResideMenu resideMenu;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private static Context context;
    public static LocationManager mLocationManager;
    public static Location mylocation;
    private static Activity thisact;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;

    SupportMapFragment mapFragment;
    ButtonFloat bf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        context = getApplicationContext();
        thisact = this;
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            bf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                }
            });

            bf = (ButtonFloat) findViewById(R.id.buttonFloat);
            bf.setRippleSpeed(30.00f);
        }
        else {
            bf.setVisibility(View.INVISIBLE);
        }
        MobileAds.initialize(this,"ca-app-pub-8384841459862317~2612006815");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        DbFirebase.setup();
        if (user != null){
            createDrawer();
            authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        startActivity(new Intent(MapsActivity.context, SignInAct.class));
                        finish();
                    }
                }
            };
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraChangeListener(CoordinateChangeListeners.cameraChangeListener);
        //GoogleMap.setClustering(ClusteringSettings);
        if (user != null){
            if (MapsActivityStart.askForPermissions(this)) {
                //TODO: screen enable permissions
            }
        }
        mMap.getUiSettings().setCompassEnabled(false);

        DbFirebase.getActiveCustomers();
        /*UserInfoClass.UserInfo info;
        try {
            info = GetMyInfo.get();
            if (info != null && !info.getNumber().equals("")){
                Log.d(TAG, "onMapReady: info got, start set up db");
                DbFirebase.setup(info);
            }
            else startActivity(new Intent(this, Settings_act.class));
        } catch (ExecutionException e) {
            e.printStackTrace();
            info = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            info = null;
        }*/
    }

    public static Double getLat(){
        if (mylocation == null) return null; // Handle null locaton
        return mylocation.getLatitude();
    }

    public static Double getLon() {
        if (mylocation == null) return null;
        return mylocation.getLongitude();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //DbFirebase.removeBykey(DbFirebase.key);
    }


    public static void upDateUsers(){
        MarkerTools.setUpClusters();
        DbFirebase.getActiveCustomers();
        Log.d(TAG, "onCreate: " + MarkerTools.clustersset);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MarkerTools.clustersset = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        upDateUsers();
    }

    public static GoogleMap getmMap() {
        return mMap;
    }

    @Override
    public void onClick(View v) {
        selectItem((int) v.getTag());
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public static Context getContext(){
        return context;
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        switch (position){
            case 0:
                DbFirebase.setTaskListener(DbFirebase.key,"0");
                startActivity(new Intent(this, MyTasksActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, Settings_act.class));
                break;

            case 2:
                startActivity(new Intent(this, About.class));
                break;

            case 3:
                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    DbFirebase.removeTask(FirebaseAuth.getInstance().getCurrentUser().getUid());
                FirebaseAuth.getInstance().signOut();
                DbUserData.user = null;
                Toast.makeText(this,"logged out",Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                Log.d(TAG, "selectItem: selected");break;
        }
    }

    public static Activity getActivity(){
        return thisact;
    }

    private void exit(){
        finish();
    }

    private void setDrawer(){
       // mPlanetTitles = getResources().getStringArray(R.array.action_titles);
       // mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      //  mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, R.id.list_text_item, mPlanetTitles));
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (resideMenu == null) return false;
        return resideMenu.dispatchTouchEvent(ev);
    }

    private void createDrawer(){
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.bg4);
        resideMenu.attachToActivity(this);
        //resideMenu.addIgnoredView(mapFragment.getView());
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setMenuListener(new ResideMenu.OnMenuListener() {
            @Override
            public void openMenu() {

            }

            @Override
            public void closeMenu() {

            }
        });
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        // create menu items;
        String titles[] = { "My tasks", "Settings", "About", "Log out"};
        int icon[] = { R.drawable.tasks,R.drawable.settings,R.drawable.info,R.drawable.logout};
        resideMenu.setScaleValue(0.6f);
        resideMenu.offsetLeftAndRight(50);
        resideMenu.setLeft(100);


        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            item.setTag(i);
            item.setBackgroundColor(Color.BLACK);
            item.setAlpha(0.8f);
            item.setOnClickListener(this);
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT);// or  ResideMenu.DIRECTION_RIGHT
        }
    }

}
