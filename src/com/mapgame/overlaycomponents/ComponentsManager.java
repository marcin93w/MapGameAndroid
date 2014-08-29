package com.mapgame.overlaycomponents;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.view.SurfaceHolder;

/*
 * Class to manage Overlay Components like car image, counters etc
 * xml components ale initialized in constructor whith activity
 * canvas components are initialized in setters whith surfaceHolders
 */
public class ComponentsManager implements SurfaceHolder.Callback {
	
	SurfaceHolder carSurfaceHolder;
	Car car;
	Double lastCarDirection;
	
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

	public void prepareCar(double azimuth) {
		lastCarDirection = azimuth;
	}
	
	public void drawCar(double azimuth) {
		if(car == null || carSurfaceHolder == null) {
			//warning - not initialized
		} else {
			lastCarDirection = azimuth;
			car.setAzimuth(azimuth);
			Canvas c = carSurfaceHolder.lockCanvas();
			car.draw(c);
			carSurfaceHolder.unlockCanvasAndPost(c);
		}
	}
	
	public void eraseCar() {
		if(carSurfaceHolder != null) {
			Canvas c = carSurfaceHolder.lockCanvas();
			c.drawColor(0, Mode.CLEAR);
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
		if(lastCarDirection != null)
			drawCar(lastCarDirection);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		callback.gameComponentsDestroyed();
		carSurfaceHolder = null;
	}
	
	
}
