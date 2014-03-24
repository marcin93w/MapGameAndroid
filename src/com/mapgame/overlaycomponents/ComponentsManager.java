package com.mapgame.overlaycomponents;

import com.mapgame.streetsgraph.DirectionVector;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ComponentsManager {
	SurfaceHolder surfaceHolder;
	
	DirectionArrow directionArrow;
	
	public ComponentsManager(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
		
		directionArrow = new DirectionArrow();
	}

	public void initDraw() {
	}
	
	public void drawDirectionArrow(DirectionVector vector) {
		directionArrow.setVector(vector);
		Canvas c = surfaceHolder.lockCanvas();
		directionArrow.draw(c);
		surfaceHolder.unlockCanvasAndPost(c);
	}
}
