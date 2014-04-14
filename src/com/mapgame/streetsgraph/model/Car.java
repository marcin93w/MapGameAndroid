package com.mapgame.streetsgraph.model;


public class Car {
	Way road;
	int pointIdx;
	
	public Car(Way road) {
		setRoad(road);
	}
	
	public Point getPoint() {
		return road.getRoad().getGeometry().get(pointIdx);
	}
	
	public Point moveAndReturnPoint() {
		pointIdx += road.isBackward() ? -1 : 1;
		return getPoint();
	}
	
	public Way getRoad() {
		return road;
	}
	
	public void setRoad(Way road) {
		this.road = road;
		pointIdx = road.isBackward() ? road.getRoad().getGeometry().size() - 1 : 0;
	}

	public boolean isOnCrossroad() {
		if(road.isBackward() && pointIdx == 0)
			return true;
		if(!road.isBackward() && pointIdx == road.getRoad().getGeometry().size()-1)
			return true;
		return false;
	}

	public int getPointIdx() {
		return pointIdx;
	}
	
}
