package com.mapgame.streetsgraph.model;

import java.io.Serializable;
import java.util.LinkedList;


public class Way implements Serializable {
	private static final long serialVersionUID = 1L;
	
	Road road;
	boolean backward;
	
	public Way(Road road, boolean backward) {
		this.road = road;
		this.backward = backward;
	}
	
	public int getEndCrossroadNode() {
		if(backward)
			return road.getStartNodeId();
		else
			return road.getEndNodeId();
	}
	
	public int getStartCrossroadNode() {
		if(backward)
			return road.getEndNodeId();
		else
			return road.getStartNodeId();
	}

	public Road getRoad() {
		return road;
	}

	public boolean isBackward() {
		return backward;
	}
	
	public Way reverse() {
		backward = !backward;
		return this;
	}
	
	public LinkedList<Point> getRoadGeometry() {
		LinkedList<Point> points = new LinkedList<Point>();
		if(!isBackward()) {
			points.addAll(road.getGeometry());
		} else {
			int pos = points.size();
			for(Point p : road.getGeometry()) {
				points.add(pos, p);
			}
		}
		
		return points;
	}
	
	public Point getFirstPoint() {
		return road.geometry.get(backward ? road.geometry.size() - 1 : 0);
	}
	
	public Point getSecondPoint() {
		return road.geometry.get(backward ? road.geometry.size() - 2 : 1);
	}
	
	public Point getLastPoint() {
		return road.geometry.get(!backward ? road.geometry.size() - 1 : 0);
	}
	
	public Point getPreLastPoint() {
		return road.geometry.get(!backward ? road.geometry.size() - 2 : 1);
	}
	
	public enum Position { START, END };
	
	public double getAzimuth(Position position) {
		if(position == Position.START)
			return getAzimuth(backward ? road.geometry.size() - 2 : 1);
		else
			return getAzimuth(!backward ? road.geometry.size() - 1 : 0);
	}
	
	public double getAzimuth(int pointId) {	
		return road.getGeometry().get(pointId + (backward ? 1 : -1)).bearingTo( 
				road.getGeometry().get(pointId));
	}
	
}
