package com.mapgame.mapprojection.previewmap;

import java.util.LinkedList;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Color;

import com.mapgame.mapprojection.AnimatedMap;
import com.mapgame.mapprojection.MapViewManageableActivity;
import com.mapgame.mapprojection.MapViewManageableActivity.MapType;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Way;

public class PreviewMap extends AnimatedMap {

	IMapController controller;
	
	Point cityCenterPoint;
	
	final int initialZoom = 11;
	
	public PreviewMap(MapViewManageableActivity mapActivity, 
			Point cityCenterPoint) {
		super(mapActivity);
		this.controller = mapActivity.getController(MapType.PREVIEW_MAP);
		this.cityCenterPoint = cityCenterPoint;
	}
	
	public void showIntroPreview(Point start, Point destination, 
			PreviewMapCallback callback) {
		controller.setZoom(initialZoom);
		controller.setCenter(cityCenterPoint);
		
		mapActivity.addStartFlagToMap(new OverlayItem("Start", "", start), MapType.PREVIEW_MAP);
		mapActivity.addEndFlagToMap(new OverlayItem("End", "", destination), MapType.PREVIEW_MAP);
		
		mapActivity.showPreviewMap();
		this.position = cityCenterPoint;
		this.moveStep = 1000;
		(new IntroPreviewMapAnimation(this, start, destination, callback)).start();
	}
	
	public void showOutroPreview(LinkedList<Way> userRoute, LinkedList<Way> bestRoute,
			PreviewMapCallback callback) {
		//TODO change map view from all city to route bounded
		controller.setZoom(initialZoom);
		controller.setCenter(cityCenterPoint);
		
		mapActivity.showPreviewMap();
		
		LinkedList<Point> points = new LinkedList<Point>();
		for(Way w : userRoute) {
			if(!w.isBackward()) {
				points.addAll(w.getRoad().getGeometry());
			} else {
				int pos = points.size();
				for(Point p : w.getRoad().getGeometry()) {
					points.add(pos, p);
				}
			}
		}
		mapActivity.addPathToPreviewMap(points, Color.RED);
		
		//FIXME change wait to onClickListener
		try {
			wait(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mapActivity.hidePreviewMap();
		callback.onPreviewFinished();
	}

}
