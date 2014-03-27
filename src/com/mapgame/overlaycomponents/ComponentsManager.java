package com.mapgame.overlaycomponents;

import com.mapgame.engine.DirectionVector;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ComponentsManager {
	
	SurfaceHolder directionArrowSurfaceHolder;
	DirectionArrow directionArrow;
	
	public ComponentsManager() {
	}

	public void initDraw() {
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
}
