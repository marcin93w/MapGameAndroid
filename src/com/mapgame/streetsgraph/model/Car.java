package com.mapgame.streetsgraph.model;


public class Car {
	Way way;
	int pointIdx;
	
	public Car(Way road) {
		setWay(road);
	}
	
	public Point getPoint() {
		return way.getRoad().getGeometry().get(pointIdx);
	}
	
	public Point moveAndReturnPoint() {
		pointIdx += way.isBackward() ? -1 : 1;
		return getPoint();
	}
	
	public Way getWay() {
		return way;
	}
	
	public void setWay(Way road) {
		this.way = road;
		pointIdx = road.isBackward() ? road.getRoad().getGeometry().size() - 1 : 0;
	}

	public boolean isOnCrossroad() {
		if(way.isBackward() && pointIdx == 0)
			return true;
		if(!way.isBackward() && pointIdx == way.getRoad().getGeometry().size()-1)
			return true;
		return false;
	}

	public int getPointIdx() {
		return pointIdx;
	}
	
}
