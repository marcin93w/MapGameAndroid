package com.mapgame.mapprojection.gamemap;

import com.mapgame.RaceActivity;
import com.mapgame.mapprojection.AnimatedMap;
import com.mapgame.streetsgraph.model.Point;

public class GameMap extends AnimatedMap {
	final int zoom = 17;
	
	MoveAnimation moveAnimation;
	
	public GameMap(RaceActivity mapActivity) {
		super(mapActivity);
		
		setSpeed(3);
		
		mapActivity.getController().setZoom(zoom);
	}

	public void setStartEndFlags(Point start, Point end) {
		((RaceActivity)mapActivity).clearOverlays();
		((RaceActivity)mapActivity).addStartFlagToMap(start.clone());
		((RaceActivity)mapActivity).addEndFlagToMap(end.clone());
		setPosition(start);
	}
	
	public void setPosition(Point position) {
		mapActivity.getController().setCenter(position);
		this.position =  position.clone();
	}
	
	public void setSpeed(int speed) {
		moveStep = speed*15;
		
		if(moveAnimation != null)
			moveAnimation.setMoveStep(moveStep);
	}

	public void moveTo(Point destination, GameMapCallback sender) {
		moveAnimation = new MoveAnimation(destination, sender);
		moveAnimation.start();
	}

	public Point stopMove() {
		moveAnimation.terminate();
		try {
			moveAnimation.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		moveAnimation = null;
		
		return position;
	}
	
}
