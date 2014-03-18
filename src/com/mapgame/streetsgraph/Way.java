package com.mapgame.streetsgraph;

import java.util.ArrayList;

public class Way {
	ArrayList<Point> geometry;
	boolean forwardEnabled;
	boolean backwardEnabled;
	
	int startNode;
	int endNode;
	
	public Way(ArrayList<Point> geometry, boolean forwardEnabled,
			boolean backwardEnabled, int startNode, int endNode) {
		super();
		this.geometry = geometry;
		this.forwardEnabled = forwardEnabled;
		this.backwardEnabled = backwardEnabled;
		this.startNode = startNode;
		this.endNode = endNode;
	}

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

	public int getStartNode() {
		return startNode;
	}

	public int getEndNode() {
		return endNode;
	}
	
}
