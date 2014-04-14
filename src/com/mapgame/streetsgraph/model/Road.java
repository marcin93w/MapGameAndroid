package com.mapgame.streetsgraph.model;

import java.util.ArrayList;
import java.util.Locale;


public class Road {
	
	public enum Class { 
		MOTORWAY,
		MOTORWAY_LINK,
		PRIMARY,
		PRIMARY_LINK,
		RESIDENTIAL,
		SECONDARY,
		SECONDARY_LINK,
		TERTIARY,
		TERTIARY_LINK,
		TRUNK,
		TRUNK_LINK,
		UNCLASSIFIED 
	};
	
	ArrayList<Point> geometry;
	boolean forwardEnabled;
	boolean backwardEnabled;

	int startNode;
	int endNode;

	double cost;
	double length;
	
	String name;
	Class roadClass;

	public Road(ArrayList<Point> geometry, boolean forwardEnabled,
			boolean backwardEnabled, int startNode, int endNode, double length,
			double cost, String name, String roadClass) {
		this.geometry = geometry;
		this.forwardEnabled = forwardEnabled;
		this.backwardEnabled = backwardEnabled;
		this.startNode = startNode;
		this.endNode = endNode;
		this.cost = cost;
		this.length = length;
		this.name = name;
		this.roadClass = Class.valueOf(roadClass.toUpperCase(Locale.US));
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

	int getStartNodeId() {
		return startNode;
	}

	int getEndNodeId() {
		return endNode;
	}

	public double getCost() {
		return cost;
	}

	public double getLength() {
		return length;
	}

	public String getName() {
		return name;
	}

	public Class getRoadClass() {
		return roadClass;
	}
	
	public boolean isMainRoad() {
		if(roadClass == Class.RESIDENTIAL || roadClass == Class.UNCLASSIFIED)
			return false;
		return true;
	}

}
