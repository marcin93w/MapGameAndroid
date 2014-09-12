package com.mapgame.mapprojection.previewmap;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;

import android.graphics.Color;

import com.mapgame.MapActivity;
import com.mapgame.RaceFinishActivity;
import com.mapgame.RacePreviewActivity;
import com.mapgame.mapprojection.AnimatedMap;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Route;

public class PreviewMap extends AnimatedMap {
	
	private final int INITIAL_ZOOM = 11;
	
	private IMapController controller;
	private MapActivity map;
	private Point cityCenterPoint;
	
	public PreviewMap(RacePreviewActivity mapActivity, 
			Point cityCenterPoint) {
		super(mapActivity);
		this.map = mapActivity;
		this.controller = mapActivity.getController();
		this.cityCenterPoint = cityCenterPoint;
	}
	
	public void showIntroPreview(Point start, Point destination, 
			PreviewMapCallback afterStartCallback, PreviewMapCallback finishCallback) {
		controller.setZoom(INITIAL_ZOOM);
		controller.setCenter(cityCenterPoint);
		
		((RacePreviewActivity)mapActivity).addStartFlagToMap(start);
		((RacePreviewActivity)mapActivity).addEndFlagToMap(destination);
		
		this.position = cityCenterPoint;
		this.moveStep = 1000;
		(new IntroPreviewMapAnimation(this, start, destination, 
				afterStartCallback, finishCallback)).start();
	}
	
	public void showOutroPreview(Point start, Point destination, 
			Route userRoute, Route bestRoute) {
		Point[] bounds = calculateRoutesRect(ListUtils.union(userRoute.getGeometry(), bestRoute.getGeometry()));
		
		map.zoomToRect(bounds);
		
		((RacePreviewActivity)mapActivity).addStartFlagToMap(start);
		((RacePreviewActivity)mapActivity).addEndFlagToMap(destination);
		
		((RaceFinishActivity)mapActivity).addPathToPreviewMap(userRoute.getGeometry(), Color.RED);
		((RaceFinishActivity)mapActivity).addPathToPreviewMap(bestRoute.getGeometry(), Color.BLUE);	
	}
	
	//returns upper-left and bottom-right corner 
	private Point[] calculateRoutesRect(List<Point> routes) {
		int minLat = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int minLon = Integer.MAX_VALUE;
		int maxLon = Integer.MIN_VALUE;

		for (GeoPoint item : routes) { 
			int lat = item.getLatitudeE6();
			int lon = item.getLongitudeE6();
			maxLat = Math.max(lat, maxLat);
			minLat = Math.min(lat, minLat);
			maxLon = Math.max(lon, maxLon);
			minLon = Math.min(lon, minLon);
		}
		
		return new Point[] { new Point(maxLat, minLon), new Point(minLat, maxLon) };
	}

}
