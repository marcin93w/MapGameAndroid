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
	DrivingEngine dc;
	RaceFinishedCallback finishedCallback;
	
	Car car;
	int turnAngle = 0;
	boolean running = false;
	CrossroadNode endNode;
	
	private LinkedList<Way> route;

	public Race(GameMap map, ComponentsManager cm, DrivingEngine dc,
			RaceFinishedCallback callback) {
		this.map = map;
		this.cm = cm;
		this.dc = dc;
		this.finishedCallback = callback;
	}

	public void initialize(CrossroadNode startNode, CrossroadNode endNode) {		
		this.car = new Car(startNode.getWay());
		this.endNode = endNode;
		this.route = new LinkedList<Way>();
		route.add(startNode.getWay());
		map.setPosition(car.getPoint());
		dc.initialize(startNode);
		cm.prepareCar(car.getWay().createDirectionVector(car.getPointIdx()));
	}
	
	public void start() {
		map.moveTo(car.moveAndReturnPoint(), this);
		running = true;
	}

	public boolean pause() {
		if(running) {
			running = false;
			return true;
		}
		return false;
	}
	
	public void unpause() {
		if(!running) {
			running = true;
			continueDrive();
		}
	}

	@Override
	public void mapMoveFinished() {
		if (running)
			continueDrive();
	}

	private void continueDrive() {
		if (!car.isOnCrossroad()) {
			map.moveTo(car.moveAndReturnPoint(), this);
			cm.drawCar(car.getWay().createDirectionVector(car.getPointIdx()));
		} else {
			if(car.getWay().getEndCrossroadNode() == endNode.getNodeId()) {
				cm.eraseCar();
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
