package com.mapgame.mapprojection;

import org.osmdroid.views.MapController;

import android.app.Activity;

import com.mapgame.streetsgraph.Point;

public class Map {
	MapController controller;
	Activity mapActivity;

	final int zoom = 17;
	//double moveStep = 0.000055;
	final int moveTimeout = 40;
	
	double moveStep;

	Point position;
	
	MoveAnimation moveAnimation;

	public enum MoveSpeed { FAST, SLOW };
	
	public Map(MapController controller, Activity mapActivity) {
		this.controller = controller;
		this.mapActivity = mapActivity;
		
		this.moveStep = 0.000055;

		controller.setZoom(zoom);
	}

	public void setPosition(Point position) {
		controller.setCenter(position);
		this.position = position;
	}
	
	public void setSpeed(MoveSpeed speed) {
		if(speed == MoveSpeed.FAST) {
			moveStep = 0.000055;
		} else {
			moveStep = 0.00002;
		}
		
		if(moveAnimation != null)
			moveAnimation.setMoveStep(moveStep);
	}

	public void moveTo(Point destination, MapMenageable sender) {
		moveAnimation = new MoveAnimation(position, destination, sender, moveStep);
		moveAnimation.start();
		position = destination;
	}

	class MoveAnimation extends Thread {
		Point start, end;
		MapMenageable sender;

		double stepsCount;
		double stepLon, stepLat;
		
		public MoveAnimation(Point start, Point end, MapMenageable sender, double moveStep) {
			this.start = start;
			this.end = end;
			this.sender = sender;
			setMoveStep(moveStep);
		}
		
		public void setMoveStep(double moveStep) {
			this.stepsCount = start.lonLatDistance(end) / moveStep;
			this.stepLat = (end.getLatitude() - start.getLatitude()) / stepsCount;
			this.stepLon = (end.getLongitude() - start.getLongitude()) / stepsCount;
		}

		@Override
		public void run() {
			Point p = start;
			while(p.isBefore(end, stepLon > 0 ? false : true, stepLat > 0 ? false : true)) {
				final Point point = p;
				mapActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						controller.setCenter(point);
					}
				});
				
				p = new Point(p.getLatitude() + stepLat, p.getLongitude() + stepLon);

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
