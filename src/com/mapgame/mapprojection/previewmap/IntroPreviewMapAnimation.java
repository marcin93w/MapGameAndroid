package com.mapgame.mapprojection.previewmap;

import org.osmdroid.api.IMapController;

import com.mapgame.mapprojection.AnimatedMap.MoveAnimation;
import com.mapgame.mapprojection.MapViewManageableActivity.MapControllerRunable;
import com.mapgame.mapprojection.MapViewManageableActivity.MapType;
import com.mapgame.mapprojection.gamemap.GameMapCallback;
import com.mapgame.streetsgraph.model.Point;

class IntroPreviewMapAnimation extends Thread {
	Point start, destination;
	PreviewMap map;
	PreviewMapCallback callback;
	
	MoveAnimation moveAnimation;
	
	public IntroPreviewMapAnimation(PreviewMap map, Point start, Point destination,
			PreviewMapCallback callback) {
		this.start = start;
		this.destination = destination;
		this.map = map;
		this.callback = callback;
	}
	
	@Override
	public void run() {
		try {
			sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		moveToStart();
		//onAnimationEnd();
	}
	
	private void moveToStart() {
		map.position = start.clone();
		map.mapActivity.invokeMapController(new MapControllerRunable() {		
			@Override
			public void run(IMapController controller) {
				controller.setCenter(start);
				controller.setZoom(14);
				controller.zoomIn();
			}
		}, MapType.PREVIEW_MAP);
		try {
			sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		moveToDestination();
	}
	
	@SuppressWarnings("unused")
	private void moveToStartSmoothly() {
		map.new MoveAnimation(start, new GameMapCallback() {
			@Override
			public void mapMoveFinished() {
				map.mapActivity.invokeMapController(new MapControllerRunable() {		
					@Override
					public void run(IMapController controller) {
						controller.zoomIn();
					}
				}, MapType.PREVIEW_MAP);
				try {
					sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				moveToDestination();						
			}
		}).start();
	}
	
	private void moveToDestination() {
		map.mapActivity.invokeMapController(new MapControllerRunable() {
			@Override
			public void run(IMapController controller) {
				controller.zoomOut();
			}
		}, MapType.PREVIEW_MAP);
		
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		map.new MoveAnimation(destination, new GameMapCallback() {
			@Override
			public void mapMoveFinished() {
				map.mapActivity.invokeMapController(new MapControllerRunable() {
					@Override
					public void run(IMapController controller) {
						controller.zoomIn();
					}
				}, MapType.PREVIEW_MAP);	
				try {
					sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				onAnimationEnd();
			}
		}).start();		
	}
	
	private void onAnimationEnd() {
		map.mapActivity.hidePreviewMap();
		callback.onPreviewFinished();
	}
}
