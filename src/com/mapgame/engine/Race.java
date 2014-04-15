package com.mapgame.engine;

import java.util.LinkedList;

import com.mapgame.mapprojection.gamemap.GameMap;
import com.mapgame.mapprojection.gamemap.GameMapCallback;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.streetsgraph.model.Car;
import com.mapgame.streetsgraph.model.CrossroadNode;
import com.mapgame.streetsgraph.model.Way;

public class Race implements GameMapCallback {
	GameMap map;
	ComponentsManager cm;
	DrivingController dc;
	RaceFinishedCallback finishedCallback;
	
	Car car;
	int turnAngle = 0;
	boolean stop = false;
	CrossroadNode endNode;
	
	private LinkedList<Way> route;

	public Race(GameMap map, ComponentsManager cm, DrivingController dc,
			RaceFinishedCallback callback) {
		this.map = map;
		this.cm = cm;
		this.dc = dc;
		this.finishedCallback = callback;
	}

	public void start(CrossroadNode startNode, CrossroadNode endNode) {		
		this.car = new Car(startNode.getWay());
		this.endNode = endNode;
		this.route = new LinkedList<Way>();
		route.add(startNode.getWay());
		map.setPosition(car.getPoint());
		map.moveTo(car.moveAndReturnPoint(), this);
		dc.initialize(startNode);
	}

	public void pause() {
		stop = true;
	}
	
	public void unpause() {
		if(stop) {
			stop = false;
			continueDrive();
		}
	}

	@Override
	public void mapMoveFinished() {
		if (!stop)
			continueDrive();
	}

	private void continueDrive() {
		if (!car.isOnCrossroad()) {
			map.moveTo(car.moveAndReturnPoint(), this);
			cm.drawCar(car.getWay().createDirectionVector(car.getPointIdx()));
		} else {
			//cm.updateCounters(car.getRoad().getRoad().getLength(), 
			//		car.getRoad().getRoad().getCost());

			if(car.getWay().getEndCrossroadNode() == endNode.getNodeId()) {
				finishedCallback.onRaceFinished(route);
			} else {
				Way nextWay = dc.getNextWay();
				car.setWay(nextWay);
				route.add(nextWay);
				continueDrive();
			}
		}

	}
}
