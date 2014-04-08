package com.mapgame.mapprojection;

import org.osmdroid.views.MapController;

import android.app.Activity;

import com.mapgame.streetsgraph.Point;

public class Map {
	MapController controller;
	Activity mapActivity;

	final int zoom = 17;
	final int moveTimeout = 40;
	
	double moveStep;

	Point position;
	
	MoveAnimation moveAnimation;

	public enum MoveSpeed { FAST, SLOW };
	
	public Map(MapController controller, Activity mapActivity) {
		this.controller = controller;
		this.mapActivity = mapActivity;
		
		setSpeed(MoveSpeed.FAST);

		controller.setZoom(zoom);
	}

	public void setPosition(Point position) {
		controller.setCenter(position);
		this.position = position;
	}
	
	public void setSpeed(MoveSpeed speed) {
		if(speed == MoveSpeed.FAST) {
			moveStep = 50;//0.000055;
		} else {
			moveStep = 15;//0.00002;
		}
		
		if(moveAnimation != null)
			moveAnimation.setMoveStep(moveStep);
	}

	public void moveTo(Point destination, MapMenageable sender) {
		moveAnimation = new MoveAnimation(destination, sender);
		moveAnimation.start();
	}

	class MoveAnimation extends Thread {
		Point end;
		MapMenageable sender;

		double stepsCount;
		double stepLon, stepLat;
		
		public MoveAnimation(Point end, MapMenageable sender) {
			this.end = end;
			this.sender = sender;
			setMoveStep(moveStep);
		}
		
		public void setMoveStep(double moveStep) {
			this.stepsCount = (int)(position.getMercatorDistance(end) / moveStep);
			if(this.stepsCount == 0)
				this.stepsCount = 1;
			this.stepLat = (end.getLatitudeE6() - position.getLatitudeE6()) / stepsCount;
			this.stepLon = (end.getLongitudeE6() - position.getLongitudeE6()) / stepsCount;
		}

		@Override
		public void run() {
			Point p = position;
			while(p.isBefore(end, stepLon > 0 ? false : true, stepLat > 0 ? false : true)) {
				final Point point = p;
				mapActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						controller.setCenter(point);
					}
				});
				
				p.setLatitudeE6(p.getLatitudeE6() + (int)stepLat);
				p.setLongitudeE6(p.getLongitudeE6() + (int)stepLon);

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
