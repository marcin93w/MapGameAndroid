package com.mapgame.streetsgraph;

import java.util.ArrayList;

public class Way {
	ArrayList<Point> geometry;
	boolean forwardEnabled;
	boolean backwardEnabled;
	
	public Way(ArrayList<Point> geometry, boolean canForward, boolean canBackward) {
		super();
		this.geometry = geometry;
		this.forwardEnabled = canForward;
		this.backwardEnabled = canBackward;
	}
	
	public ArrayList<Point> getGeometry() {
		return geometry;
	}
	
	public boolean isForwardEnabled() {
		return forwardEnabled;
	}
	
	public boolean isBackwardEnabled() {
		return backwardEnabled;
	}
	
}
