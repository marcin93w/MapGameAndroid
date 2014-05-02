package com.mapgame.streetsgraph.model;

import java.io.Serializable;
import java.util.LinkedList;

public class Route implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LinkedList<Point> geometry;
	private double cost;
	private double length;
	
	public Route(LinkedList<Point> geometry, double cost, double length) {
		this.geometry = geometry;
		this.cost = cost;
		this.length = length;
	}
	
	public Route(LinkedList<Way> route) {
		geometry = new LinkedList<Point>();
		for(Way way : route) {
			pushWay(way);
		}
	}

	public void pushWay(Way way) {
		geometry.addAll(way.getRoadGeometry());
		cost += way.getRoad().getCost();
		length += way.getRoad().getLength();
	}
	
	public void addWayToBegin(Way way) {
		geometry.addAll(0, way.getRoadGeometry());
		cost += way.getRoad().getCost();
		length += way.getRoad().getLength();
	}
	
	public LinkedList<Point> getGeometry() {
		return geometry;
	}

	public double getCost() {
		return cost;
	}

	public double getLength() {
		return length;
	}

}
