package com.mapgame.streetsgraph.model;

/*
 * Car position
 */
public class Car {
	Way way;
	int pointIdx;
	
	public Car(Way road) {
		setWay(road);
	}
	
	public Point getPoint() {
		return way.getRoad().getGeometry().get(pointIdx);
	}
	
	public int moveAndReturnPointIdx() {
		return pointIdx += way.isBackward() ? -1 : 1;
	}
	
	public Point moveAndReturnPoint() {
		pointIdx += way.isBackward() ? -1 : 1;
		return getPoint();
	}
	
	public Point moveBackAndReturnPoint() {
		pointIdx += way.isBackward() ? 1 : -1;
		return getPoint();
	}
	
	public Way getWay() {
		return way;
	}
	
	public void setWay(Way road) {
		this.way = road;
		pointIdx = road.isBackward() ? road.getRoad().getGeometry().size() - 1 : 0;
	}
	
	public void setBackWay(Way road) {
		this.way = road;
		pointIdx = road.isBackward() ? 0 : road.getRoad().getGeometry().size() - 1;
	}

	public boolean isOnEndCrossroad() {
		if(way.isBackward() && pointIdx == 0)
			return true;
		if(!way.isBackward() && pointIdx == way.getRoad().getGeometry().size()-1)
			return true;
		return false;
	}
	
	public boolean isOnStartCrossroad() {
		if(!way.isBackward() && pointIdx == 0)
			return true;
		if(way.isBackward() && pointIdx == way.getRoad().getGeometry().size()-1)
			return true;
		return false;
	}
	
	public boolean isOnCrossroad() {
		if(pointIdx == 0 || pointIdx == way.getRoad().getGeometry().size()-1)
			return true;
		return false;
	}

	public int getPointIdx() {
		return pointIdx;
	}
	
}
