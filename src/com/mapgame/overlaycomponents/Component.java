package com.mapgame.overlaycomponents;

import android.content.res.Resources;
import android.graphics.Canvas;

public abstract class Component {	
	Resources resources;
	
	public Component(Resources resources) {
		this.resources = resources;
	}
	
	abstract void draw(Canvas canvas);
	
	protected Resources getResources() {
		return resources;
	}
}
