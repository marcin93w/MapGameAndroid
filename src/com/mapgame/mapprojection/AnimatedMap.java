package com.mapgame.mapprojection;

import org.osmdroid.views.MapController;

import com.mapgame.streetsgraph.Point;

import android.app.Activity;

public class AnimatedMap {
	MapController controller;
	Activity mapActivity;
	
	//for animation
	double moveStep;
	Point position;
	
	final int moveTimeout = 40;
	
	public AnimatedMap(MapController controller, Activity mapActivity) {
		this.controller = controller;
		this.mapActivity = mapActivity;
	}
	
	class MoveAnimation extends Thread {
		Point end;
		MapMoveMenageable sender;

		double stepsCount;
		double stepLon, stepLat;
		
		public MoveAnimation(Point end, MapMoveMenageable sender) {
			this.end = end;
			this.sender = sender;
			setMoveStep(moveStep);
		}
		
		public void setMoveStep(double moveStep) {
			this.stepsCount = (position.getLonLatDistance(end) / moveStep);
			if(this.stepsCount == 0)
				this.stepsCount = 1;
			this.stepLat = (end.getLatitudeE6() - position.getLatitudeE6()) / stepsCount;
			this.stepLon = (end.getLongitudeE6() - position.getLongitudeE6()) / stepsCount;
		}

		@Override
		public void run() {
			while(position.isBefore(end, stepLon > 0 ? false : true, stepLat > 0 ? false : true)) {
				mapActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						controller.setCenter(position);
					}
				});
				
				position.setLatitudeE6(position.getLatitudeE6() + (int)stepLat);
				position.setLongitudeE6(position.getLongitudeE6() + (int)stepLon);

				try {
					sleep(moveTimeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			sender.mapMoveFinished();
		}
	}
	
}
