package com.mapgame.mapprojection;

import java.util.ArrayList;

import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.view.View;

import com.mapgame.R;
import com.mapgame.streetsgraph.Point;

public class PreviewMap extends AnimatedMap {

	View mapViewLayout;
	MapView mapView;
	Point cityCenterPoint;
	
	final int initialZoom = 11;
	
	public PreviewMap(MapView mapView, Activity mapActivity, 
			Point cityCenterPoint) {
		super((MapController)mapView.getController(), mapActivity);
		mapViewLayout = mapActivity.findViewById(R.id.mapprevievLayout);
		this.mapView = mapView;
		this.cityCenterPoint = cityCenterPoint;
		this.position = cityCenterPoint;
		this.moveStep = 1000;
	}
	
	public void showIntroPreview(Point start, Point destination, 
			PreviewMapManageable callback) {
		controller.setZoom(initialZoom);
		controller.setCenter(cityCenterPoint);
		
		final ArrayList<OverlayItem> startItem = new ArrayList<OverlayItem>();
		startItem.add(new OverlayItem("Start", "SampleDescription", start));
		ItemizedIconOverlay<OverlayItem> startLocationOverlay = 
				new ItemizedIconOverlay<OverlayItem>(startItem, 
						mapActivity.getResources().getDrawable(R.drawable.start_flag) ,null,
						new ResourceProxyImpl(mapActivity.getApplicationContext()));
		
		final ArrayList<OverlayItem> endItem = new ArrayList<OverlayItem>();
		endItem.add(new OverlayItem("End", "SampleDescription", destination));	
		ItemizedIconOverlay<OverlayItem> endLocationOverlay = 
				new ItemizedIconOverlay<OverlayItem>(endItem, 
						mapActivity.getResources().getDrawable(R.drawable.end_flag) ,null,
						new ResourceProxyImpl(mapActivity.getApplicationContext()));
		
		this.mapView.getOverlays().add(startLocationOverlay);
		this.mapView.getOverlays().add(endLocationOverlay);
		
		mapViewLayout.setVisibility(View.VISIBLE);
		(new IntroPreviewMapAnimation(this, start, destination, callback)).start();
	}

}
