package com.mapgame.mapprojection.gamemap;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.mapgame.RaceActivity;
import com.mapgame.mapprojection.AnimatedMap;
import com.mapgame.streetsgraph.model.Point;

public class GameMap extends AnimatedMap implements OnCheckedChangeListener {
	final int zoom = 17;
	
	MoveAnimation moveAnimation;

	public enum MoveSpeed { FAST, SLOW };
	
	public GameMap(RaceActivity mapActivity) {
		super(mapActivity);
		
		setSpeed(MoveSpeed.FAST);
		
		mapActivity.getController().setZoom(zoom);
	}

	public void setStartEnd(Point start, Point end) {
		((RaceActivity)mapActivity).addStartFlagToMap(start.clone());
		((RaceActivity)mapActivity).addEndFlagToMap(end.clone());
		setPosition(start);
	}
	
	public void setPosition(Point position) {
		mapActivity.getController().setCenter(position);
		this.position =  position.clone();
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
