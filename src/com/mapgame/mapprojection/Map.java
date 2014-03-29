package com.mapgame.mapprojection;

import java.util.ArrayList;

import org.osmdroid.views.MapController;

import android.app.Activity;

import com.mapgame.streetsgraph.Point;

public class Map {
	MapController controller;
	Activity mapActivity;

	final int zoom = 17;
	final double moveStep = 0.000055;
	final int moveTimeout = 40;

	Point position;

	public Map(MapController controller, Activity mapActivity) {
		this.controller = controller;
		this.mapActivity = mapActivity;

		controller.setZoom(zoom);
	}

	public void setPosition(Point position) {
		controller.setCenter(position);
		this.position = position;
	}

	public void moveTo(Point destination, MapMenageable sender) {
		ArrayList<Point> movePoints = new ArrayList<Point>();
		int steps = (int) (destination.lonLatDistance(position) / moveStep);

		for (int i = 0; i < steps; i++) {
			movePoints.add(new Point(
					position.getLatitude()
					+ ((destination.getLatitude() - position.getLatitude())
							/ steps * (i + 1)), 
					position.getLongitude()
					+ ((destination.getLongitude() - position.getLongitude())
							/ steps * (i + 1))
			));
		}

		(new MoveAnimation(movePoints, sender)).start();
		position = destination;
	}

	class MoveAnimation extends Thread {
		ArrayList<Point> movePoints;
		MapMenageable sender;

		public MoveAnimation(ArrayList<Point> movePoints, MapMenageable sender) {
			super();
			this.movePoints = movePoints;
			this.sender = sender;
		}

		@Override
		public void run() {
			for (final Point point : movePoints) {
				mapActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						controller.setCenter(point);
					}
				});

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
