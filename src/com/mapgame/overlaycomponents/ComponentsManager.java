package com.mapgame.overlaycomponents;

import com.mapgame.engine.DirectionVector;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/*
 * Class to manage Overlay Components like car image, counters etc
 * xml components ale initialized in constructor whith activity
 * canvas components are initialized in setters whith surfaceHolders
 */
public class ComponentsManager {
	
	SurfaceHolder directionArrowSurfaceHolder;
	DirectionArrow directionArrow;
	
	LengthCounter lengthCounter;
	
	public ComponentsManager(Activity mainActivity) {
		lengthCounter = new LengthCounter(mainActivity);
	}
	
	public void setDirectionArrowSurfaceHolder(
			SurfaceHolder directionArrowSurfaceHolder) {
		this.directionArrowSurfaceHolder = directionArrowSurfaceHolder;
		directionArrow = new DirectionArrow();
	}

	public void drawDirectionArrow(DirectionVector vector) {
		if(directionArrow == null || directionArrowSurfaceHolder == null) {
			//warning - not initialized
		} else {
			directionArrow.setVector(vector);
			Canvas c = directionArrowSurfaceHolder.lockCanvas();
			directionArrow.draw(c);
			directionArrowSurfaceHolder.unlockCanvasAndPost(c);
		}
	}
	
	public void updateCounters(double length, double cost) {
		lengthCounter.addLength(length);
		lengthCounter.addCost(cost);
	}
}
