package com.mapgame.mapprojection.previewmap;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.overlay.OverlayItem;

import com.mapgame.mapprojection.AnimatedMap;
import com.mapgame.mapprojection.MapViewManageableActivity;
import com.mapgame.mapprojection.MapViewManageableActivity.MapType;
import com.mapgame.streetsgraph.model.Point;

public class PreviewMap extends AnimatedMap {

	IMapController controller;
	
	Point cityCenterPoint;
	
	final int initialZoom = 11;
	
	public PreviewMap(MapViewManageableActivity mapActivity, 
			Point cityCenterPoint) {
		super(mapActivity);
		this.controller = mapActivity.getController(MapType.PREVIEW_MAP);
		this.cityCenterPoint = cityCenterPoint;
		this.position = cityCenterPoint;
		this.moveStep = 1000;
	}
	
	public void showIntroPreview(Point start, Point destination, 
			PreviewMapCallback callback) {
		controller.setZoom(initialZoom);
		controller.setCenter(cityCenterPoint);
		
		mapActivity.addStartFlagToMap(new OverlayItem("Start", "", start), MapType.PREVIEW_MAP);
		mapActivity.addEndFlagToMap(new OverlayItem("End", "", destination), MapType.PREVIEW_MAP);
		
		mapActivity.showPreviewMap();
		(new IntroPreviewMapAnimation(this, start, destination, callback)).start();
	}

}
