package com.mapgame.mapprojection;

import android.view.View;

import com.mapgame.mapprojection.AnimatedMap.MoveAnimation;
import com.mapgame.streetsgraph.Point;

class IntroPreviewMapAnimation extends Thread {
	Point start, destination;
	PreviewMap map;
	PreviewMapManageable callback;
	
	MoveAnimation moveAnimation;
	
	public IntroPreviewMapAnimation(PreviewMap map, Point start, Point destination,
			PreviewMapManageable callback) {
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
	}
	
	private void moveToStart() {
		map.position = start.clone();
		map.mapActivity.runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				map.controller.setCenter(start);
				map.controller.setZoom(14);
				map.controller.zoomIn();
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
		map.new MoveAnimation(start, new MapMoveMenageable() {
			@Override
			public void mapMoveFinished() {
				map.mapActivity.runOnUiThread(new Runnable() {		
					@Override
					public void run() {
						map.controller.zoomIn();
					}
				});
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
		map.mapActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				map.controller.zoomOut();
			}
		});
		
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		map.new MoveAnimation(destination, new MapMoveMenageable() {
			@Override
			public void mapMoveFinished() {
				map.mapActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						map.controller.zoomIn();
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
		map.mapActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				map.mapViewLayout.setVisibility(View.GONE);
			}
		});
		callback.previewFinished();
	}
}
