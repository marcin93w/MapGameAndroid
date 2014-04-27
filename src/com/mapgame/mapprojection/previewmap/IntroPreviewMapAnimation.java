package com.mapgame.mapprojection.previewmap;

import org.osmdroid.api.IMapController;

import com.mapgame.RacePreviewActivity;
import com.mapgame.mapprojection.AnimatedMap.MoveAnimation;
import com.mapgame.mapprojection.MapViewManageableActivity.MapControllerRunable;
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
		});
		try {
			sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		moveToDestination();
	}
	
	private void moveToDestination() {
		map.mapActivity.invokeMapController(new MapControllerRunable() {
			@Override
			public void run(IMapController controller) {
				controller.zoomOut();
			}
		});
		
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		map.new MoveAnimation(null, destination, new GameMapCallback() {
			@Override
			public void mapMoveFinished() {
				map.mapActivity.invokeMapController(new MapControllerRunable() {
					@Override
					public void run(IMapController controller) {
						controller.zoomIn();
					}
				});	
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
		((RacePreviewActivity)map.mapActivity).invokeEndPreview();
		callback.onPreviewFinished();
	}
}
