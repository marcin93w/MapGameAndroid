package com.mapgame.overlaycomponents;

import com.mapgame.engine.DirectionVector;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/*
 * Class to manage Overlay Components like car image, counters etc
 * xml components ale initialized in constructor whith activity
 * canvas components are initialized in setters whith surfaceHolders
 */
public class ComponentsManager {
	
	SurfaceHolder carSurfaceHolder;
	Car car;
	
	LengthCounter lengthCounter;
	
	public ComponentsManager(Activity mainActivity) {
		lengthCounter = new LengthCounter(mainActivity);
	}
	
	public void setCarSurfaceHolder(
			SurfaceHolder carSurfaceHolder, Resources resources) {
		this.carSurfaceHolder = carSurfaceHolder;
		car = new Car(resources);
	}

	public void drawCar(DirectionVector vector) {
		if(car == null || carSurfaceHolder == null) {
			//warning - not initialized
		} else {
			car.setVector(vector);
			Canvas c = carSurfaceHolder.lockCanvas();
			car.draw(c);
			carSurfaceHolder.unlockCanvasAndPost(c);
		}
	}
	
	public void updateCounters(double length, double cost) {
		lengthCounter.addLength(length);
		lengthCounter.addCost(cost);
	}
}
