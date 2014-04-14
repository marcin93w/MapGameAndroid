package com.mapgame.overlaycomponents;

import com.mapgame.streetsgraph.model.DirectionVector;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/*
 * Class to manage Overlay Components like car image, counters etc
 * xml components ale initialized in constructor whith activity
 * canvas components are initialized in setters whith surfaceHolders
 */
public class ComponentsManager implements SurfaceHolder.Callback {
	
	SurfaceHolder carSurfaceHolder;
	Car car;
	
	Resources resources;
	GameComponentsCallback callback;
	
	public ComponentsManager(Resources resources, GameComponentsCallback callback) {
		this.resources = resources;
		this.callback = callback;
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

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		setCarSurfaceHolder(holder, resources);
		callback.gameComponentsCreated();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		callback.gameComponentsDestroyed();
	}
	
	
}
