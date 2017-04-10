package com.example.angry.project2345;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by S1 on 3/1/2017.
 */
public class ClusteredMarker implements ClusterItem {
    private final LatLng mPosition;
    protected MarkerOptions marker;
    private String title="";

    public ClusteredMarker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }
    public ClusteredMarker(MarkerOptions mm){
        mPosition = mm.getPosition();
        this.marker = mm;
        title = mm.getTitle();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public void setMm(MarkerOptions mm) {
        this.marker = mm;
    }

    public MarkerOptions getMm() {
        return marker;
    }
}