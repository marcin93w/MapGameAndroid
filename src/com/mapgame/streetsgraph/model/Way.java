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
	
	public Point getLastPoint() {
		return road.geometry.get(!backward ? road.geometry.size() - 1 : 0);
	}
	
	public enum Position { START, END };
	
	public DirectionVector getDirectionVector(Position position) {
		if(position == Position.START)
			return createDirectionVector(0);
		else
			return createDirectionVector(road.getGeometry().size()-1);
	}
	
	public DirectionVector createDirectionVector(int pointId) {
		Point a,b;
		if(pointId < road.getGeometry().size()-1) {
			a = road.getGeometry().get(pointId); 
			b = road.getGeometry().get(pointId+1);
		} else {
			a = road.getGeometry().get(pointId-1);
			b = road.getGeometry().get(pointId);
		}
		
		if(backward) {
			return (new DirectionVector(b,a));
		} else {
			return (new DirectionVector(a,b));
		}
	}
	
}
