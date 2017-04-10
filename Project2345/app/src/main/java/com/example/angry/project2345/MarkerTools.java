package com.example.angry.project2345;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

/**
 * Created by S1 on 2/13/2017.
 */


public class MarkerTools {
    public static Handler h;
    private static ClusteredMarker chosenMarker = null;
    public static UserInfoClass.TaskInfo tinfo;
    private static Timer timer;
    public static boolean clustersset = false;


    public static void setUpClusters(){
        if (MapsActivity.getmMap() == null) return;
        if (!clustersset) {
            Log.d("setupclusters", "addMarker: set up");
            setUpClusterer();
        }
    }

    public static void addMarker(UserInfoClass.TaskInfo usr, String key) {
        setUpClusters();
        if (usr == null) return;
        MarkerOptions mm;
        if (usr.getLat() == null || usr.getLon() == null) return;
        LatLng asideusrpos = new LatLng(usr.getLat(), usr.getLon());
        if (usr.getLat() == null || usr.getLon() == null) return;
        if (usr.getName() == null || usr.getName().equals("")){

            mm = new MarkerOptions().title(usr.getuId()).position(asideusrpos)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        else {
            mm = new MarkerOptions()
                    .position(asideusrpos)
                    .title(usr.getuId())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                     Date d = new Date();
                     int x = d.getMinutes() - usr.getDate().getMinutes();
                     if (x >= Constants.alphatime) mm.alpha(0.4f);
                    else mm.alpha(1 - (Constants.alphak * x));
                    Log.d("addMarker", "addMarker: set coloured icon");

        }
        if (usr != null) {

        }
        addmarker(mm);
/*       MapsActivity.getmMap().addMarker(mm);
                //.icon(BitmapDescriptorFactory.fromBitmap(preparemarker())));//icon
 */
    }

    public static ClusterManager<ClusteredMarker> mClusterManager;

    private static void setUpClusterer() {
        // Declare a variable for the cluster manager.
        clustersset = true;
        GoogleMap map = MapsActivity.getmMap();
        // Position the map.
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<ClusteredMarker>(MapsActivity.getContext(), map);


        mClusterManager.setRenderer(new OwnIconRender(
                MapsActivity.getContext(), MapsActivity.getmMap(), mClusterManager));

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusteredMarker>() {
            @Override
            public boolean onClusterItemClick(ClusteredMarker item) {
                Log.d("clusterselect", "onClusterItemClick: selected");
                chosenMarker = item;
                return false;
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<ClusteredMarker>() {
            @Override
            public void onClusterItemInfoWindowClick(ClusteredMarker clusteredMarker) {
                if (tinfo == null) return;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tinfo.getName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(MapsActivity.getContext(),
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                MapsActivity.getContext().startActivity(intent);
            }
        });
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.

        map.setOnCameraIdleListener(mClusterManager);
     //   map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                if (chosenMarker==null) return null;
                Context context = MapsActivity.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View view = inflater.inflate(R.layout.task_info_marker, null, false);
                itdesc = (TextView) view.findViewById(R.id.info_task_desc);
                ittitle = (TextView) view.findViewById(R.id.info_task_title);
                itpayment = (TextView) view.findViewById(R.id.info_task_payment);
                itpb = (ProgressBar) view.findViewById(R.id.info_task_progressbar);
                ittaskcalllabel = (TextView) view.findViewById(R.id.info_task_label_call);
                if (fl){
                    fl=false;
                    Log.d("handler", "handleMessage: Value received");
                    itpb.setVisibility(View.INVISIBLE);
                    ittaskcalllabel.setVisibility(View.VISIBLE);

                 /*   if (tinfo == null) Log.d("handleitemclick", "getInfoWindow: tinfo == null");
                    if (tinfo.getTasksInfo() == null) Log.d("handleitemclick", "getInfoWindow: tinfotasks == null");*/

                    //UserInfoClass.TaskInfo arrt = tinfo;
                    if (tinfo != null && tinfo.getName().equals("")){
                        ittitle.setText("Looking for a job");
                        itdesc.setText("");
                        itpayment.setText("");
                        tinfo = null;
                        return view;
                    }
                    UserInfoClass.TaskInfo t = (UserInfoClass.TaskInfo) tinfo;
                    itdesc.setText(t.getDescription());
                    ittitle.setText(t.getName());
                    itpayment.setText(t.getReward());
                    DbFirebase.removeTaskInfoListener();
                    return view;
                }
                if (h == null){
                    updateInfoViewAfterLoading();
                }
                m = marker;
                if (itdesc == null || ittitle == null || itpayment == null){
                    Toast.makeText(MapsActivity.getContext(),
                            "Error occured while inflating view",
                            Toast.LENGTH_SHORT).show();
                }
                itdesc.setVisibility(View.INVISIBLE);
                itpayment.setVisibility(View.INVISIBLE);
                ittitle.setText("Loading data");
                itpb.setVisibility(View.VISIBLE);
                ittaskcalllabel.setVisibility(View.INVISIBLE);
                DbFirebase.loadDataTaskView(chosenMarker.getTitle(),"");
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        map.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        map.setOnInfoWindowClickListener(mClusterManager);
    }


    private static void addmarker(MarkerOptions mm){
        /*lat = lat + offset;
        lng = lng + offset;*/
        Log.d("markeradding", "addmarker: marker has been added");
        ClusteredMarker offsetItem = new ClusteredMarker(mm);
        mClusterManager.addItem(offsetItem);
    }

    public static Bitmap preparemarker(){
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(15f,15f,10f,new Paint());
        return bitmap;
    }



    private static TextView itdesc,ittitle,itpayment,ittaskcalllabel;
    private static Marker m;
    private static boolean fl=false;
    private static ProgressBar itpb;


    private static void updateInfoViewAfterLoading(){
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        tinfo = (UserInfoClass.TaskInfo) msg.obj;
                        fl = true;
                        m.showInfoWindow();
                        break;
                    default: break;
                }
            }
        };
    }
}