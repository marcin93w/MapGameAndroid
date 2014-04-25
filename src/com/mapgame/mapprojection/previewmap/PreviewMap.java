package com.mapgame.mapprojection.previewmap;

import java.util.LinkedList;

import org.osmdroid.api.IMapController;

import android.graphics.Color;

import com.mapgame.RaceFinishActivity;
import com.mapgame.RacePreviewActivity;
import com.mapgame.mapprojection.AnimatedMap;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Way;

public class PreviewMap extends AnimatedMap {

	IMapController controller;
	
	Point cityCenterPoint;
	
	final int initialZoom = 11;
	
	public PreviewMap(RacePreviewActivity mapActivity, 
			Point cityCenterPoint) {
		super(mapActivity);
		this.controller = mapActivity.getController();
		this.cityCenterPoint = cityCenterPoint;
	}
	
	public void showIntroPreview(Point start, Point destination, 
			PreviewMapCallback callback) {
		controller.setZoom(initialZoom);
		controller.setCenter(cityCenterPoint);
		
		((RacePreviewActivity)mapActivity).addStartFlagToMap(start);
		((RacePreviewActivity)mapActivity).addEndFlagToMap(destination);
		
		this.position = cityCenterPoint;
		this.moveStep = 1000;
		(new IntroPreviewMapAnimation(this, start, destination, callback)).start();
	}
	
	public void showOutroPreview(Point start, Point destination, 
			LinkedList<Way> userRoute, LinkedList<Point> bestRoute) {
		//TODO change map view from all city to route bounded
		controller.setZoom(initialZoom);
		controller.setCenter(cityCenterPoint);
		
		((RacePreviewActivity)mapActivity).addStartFlagToMap(start);
		((RacePreviewActivity)mapActivity).addEndFlagToMap(destination);

		
		LinkedList<Point> points = new LinkedList<Point>();
		for(Way w : userRoute) {
			points.addAll(w.getRoadGeometry());
		}
		
		((RaceFinishActivity)mapActivity).addPathToPreviewMap(points, Color.RED);
		((RaceFinishActivity)mapActivity).addPathToPreviewMap(bestRoute, Color.BLUE);	
	}

}
