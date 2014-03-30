package com.mapgame.engine;

import com.mapgame.streetsgraph.Point;
import com.mapgame.streetsgraph.Road;

public class Car {
	Road road;
	int pointIdx;
	
	public Car(Road road) {
		setRoad(road);
	}
	
	public Point getPoint() {
		return road.getWay().getGeometry().get(pointIdx);
	}
	
	public Point move() {
		pointIdx += road.isBackward() ? -1 : 1;
		return getPoint();
	}
	
	public Road getRoad() {
		return road;
	}
	
	public void setRoad(Road road) {
		this.road = road;
		pointIdx = road.isBackward() ? road.getWay().getGeometry().size() - 1 : 0;
	}

	public boolean isOnCrossroad() {
		if(road.isBackward() && pointIdx == 0)
			return true;
		if(!road.isBackward() && pointIdx == road.getWay().getGeometry().size()-1)
			return true;
		return false;
	}
	
}
