package com.mapgame.mapprojection.gamemap;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.mapgame.mapprojection.AnimatedMap;
import com.mapgame.mapprojection.MapViewManageableActivity;
import com.mapgame.mapprojection.MapViewManageableActivity.MapType;
import com.mapgame.streetsgraph.model.Point;

public class GameMap extends AnimatedMap implements OnCheckedChangeListener {
	final int zoom = 17;
	
	MoveAnimation moveAnimation;

	public enum MoveSpeed { FAST, SLOW };
	
	public GameMap(MapViewManageableActivity mapActivity) {
		super(mapActivity);
		setSpeed(MoveSpeed.FAST);
		
		mapActivity.getController(MapType.GAME_MAP).setZoom(zoom);
	}

	public void setPosition(Point position) {
		mapActivity.getController(MapType.GAME_MAP).setCenter(position);
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

	public void moveTo(Point destination, GameMapCallback sender) {
		moveAnimation = new MoveAnimation(destination, sender);
		moveAnimation.start();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked) {
			setSpeed(MoveSpeed.SLOW);
		} else {
			setSpeed(MoveSpeed.FAST);
		}
	}

}
