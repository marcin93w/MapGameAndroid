package com.mapgame.streetsgraph;

import java.util.ArrayList;

public class Way {
	ArrayList<Point> geometry;
	boolean forwardEnabled;
	boolean backwardEnabled;

	int startNode;
	int endNode;

	double cost;
	double length;

	public Way(ArrayList<Point> geometry, boolean forwardEnabled,
			boolean backwardEnabled, int startNode, int endNode, double length,
			double cost) {
		this.geometry = geometry;
		this.forwardEnabled = forwardEnabled;
		this.backwardEnabled = backwardEnabled;
		this.startNode = startNode;
		this.endNode = endNode;
		this.cost = cost;
		this.length = length;
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

	public double getCost() {
		return cost;
	}

	public double getLength() {
		return length;
	}

}
