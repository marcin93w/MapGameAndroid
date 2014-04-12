package com.mapgame.mapprojection;

import org.osmdroid.views.MapController;

import android.app.Activity;

import com.mapgame.streetsgraph.Point;

public class GameMap extends AnimatedMap {
	final int zoom = 17;
	
	MoveAnimation moveAnimation;

	public enum MoveSpeed { FAST, SLOW };
	
	public GameMap(MapController controller, Activity mapActivity) {
		super(controller, mapActivity);
		setSpeed(MoveSpeed.FAST);

		controller.setZoom(zoom);
	}

	public void setPosition(Point position) {
		controller.setCenter(position);
		this.position =  position;
	}
	
	public void setSpeed(MoveSpeed speed) {
		if(speed == MoveSpeed.FAST) {
			moveStep = 50;
		} else {
			moveStep = 15;
		}
		
		if(moveAnimation != null)
			moveAnimation.setMoveStep(moveStep);
	}

	public void moveTo(Point destination, MapMoveMenageable sender) {
		moveAnimation = new MoveAnimation(destination, sender);
		moveAnimation.start();
	}

}
