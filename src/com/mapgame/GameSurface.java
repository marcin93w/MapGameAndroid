package com.mapgame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mapgame.overlaycomponents.ComponentsManager;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

	ComponentsManager cm;
	MainActivity mainActivity;
	
	public GameSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		cm = new ComponentsManager(holder);
		cm.initDraw();
		mainActivity.onGameSurfaceCreated();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		mainActivity.onGameSurfaceDestroyed();	
	}
	
	public ComponentsManager getComponentsManager() {
		return cm;
	}

}
