package com.example.angry.project2345;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by S1 on 3/10/2017.
 */
public class OwnIconRender extends DefaultClusterRenderer<ClusteredMarker> {

    public OwnIconRender(Context context, GoogleMap map,
                         ClusterManager<ClusteredMarker> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusteredMarker item,
                                               MarkerOptions markerOptions) {
        markerOptions.icon(item.getMm().getIcon());
    }

}