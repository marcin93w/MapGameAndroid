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
	}
	
	public void showPreview(Point start, Point destination, 
			PreviewMapManageable callback) {
		controller.setZoom(initialZoom);
		controller.setCenter(cityCenterPoint);
		
		final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
		items.add(new OverlayItem("Start", "SampleDescription", start));
		items.add(new OverlayItem("End", "SampleDescription", destination));
		ItemizedIconOverlay<OverlayItem> mMyLocationOverlay = 
				new ItemizedIconOverlay<OverlayItem>(items, null,
						new ResourceProxyImpl(mapActivity.getApplicationContext()));
		this.mapView.getOverlays().add(mMyLocationOverlay);
		
		mapViewLayout.setVisibility(View.VISIBLE);
		(new MapPreviewAnimation(start, destination, callback)).start();
	}
	
	class MapPreviewAnimation extends Thread {
		Point start, destination;
		PreviewMapManageable callback;
		
		MoveAnimation moveAnimation;
		
		public MapPreviewAnimation(Point start, Point destination,
				PreviewMapManageable callback) {
			this.start = start;
			this.destination = destination;
			this.callback = callback;
			position = cityCenterPoint;
			moveStep = 1000;
		}
		
		@Override
		public void run() {
			try {
				sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			moveToStart();
		}
		
		private void moveToStart() {
			position = start.clone();
			mapActivity.runOnUiThread(new Runnable() {		
				@Override
				public void run() {
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
		
		@SuppressWarnings("unused")
		private void moveToStartSmoothly() {
			(new MoveAnimation(start, new MapMoveMenageable() {
				@Override
				public void mapMoveFinished() {
					mapActivity.runOnUiThread(new Runnable() {		
						@Override
						public void run() {
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
			})).start();
		}
		
		private void moveToDestination() {
			mapActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					controller.zoomOut();
				}
			});
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			(new MoveAnimation(destination, new MapMoveMenageable() {
				@Override
				public void mapMoveFinished() {
					mapActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
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
			})).start();		
		}
		
		private void onAnimationEnd() {
			mapActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mapViewLayout.setVisibility(View.GONE);
				}
			});
			callback.previewFinished();
		}
	}

}
