package com.mapgame.engine;

import java.util.LinkedList;

import android.annotation.TargetApi;
import android.os.Build;

import com.mapgame.mapprojection.gamemap.GameMap;
import com.mapgame.mapprojection.gamemap.GameMapCallback;
import com.mapgame.mapprojection.gamemap.SpeedChangeListener;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.streetsgraph.model.Car;
import com.mapgame.streetsgraph.model.CrossroadNode;
import com.mapgame.streetsgraph.model.Way;
import com.mapgame.streetsgraph.model.Way.Position;

@TargetApi(Build.VERSION_CODES.GINGERBREAD) 
public class Race implements GameMapCallback, SpeedChangeListener {
	GameMap map;
	ComponentsManager cm;
	DrivingEngine dc;
	RaceFinishedCallback finishedCallback;
	
	Car car;
	int turnAngle = 0;
	boolean running = false;
	CrossroadNode endNode;
	
	boolean runningBack = false;
	double rewindedLength = 0;
	final static double maxRewindedLength = 300;
	
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
		cm.prepareCar(car.getWay().getAzimuth(Position.START));
	}
	
	public void start() {
		map.moveTo(car.moveAndReturnPoint(), this);
		running = true;
	}

	public boolean pause() {
		if(running) {
			running = false;
			map.stopMove();
			return true;
		}
		return false;
	}
	
	public void unpause() {
		if(!running) {
			running = true;
			mapMoveFinished();
		}
	}

	@Override
	public void mapMoveFinished() {
		if (running) {
			if(!runningBack) {
				continueDrive();
			} else {
				undoDrive();
			}
		}
	}

	private void continueDrive() {
		if (!car.isOnEndCrossroad()) {
			map.moveTo(car.moveAndReturnPoint(), this);
			cm.drawCar(car.getWay().getAzimuth(car.getPointIdx()));
		} else {
			if(car.getWay().getEndCrossroadNodeId() == endNode.getNodeId()) {
				cm.eraseCar();
				running = false;
				finishedCallback.onRaceFinished(route);
			} else {
				Way nextWay = dc.getNextWay();
				car.setWay(nextWay);
				route.add(nextWay);
				decreaseRewindedLength(nextWay.getRoad().getLength());
				continueDrive();
			}
		}
	}
	
	private void undoDrive() {
		if(!car.isOnStartCrossroad()) {
			cm.drawCar(car.getWay().getAzimuth(car.getPointIdx()));
			map.moveTo(car.moveBackAndReturnPoint(), this);
			
		} else {
			if(route.peekLast() != null) {
				if(increaseRewindedLength(route.peekLast().getRoad().getLength())) {
					car.setBackWay(route.pollLast());
					undoDrive();
				}
			}
		}
	}
	
	private boolean increaseRewindedLength(double length) {
		if(rewindedLength < maxRewindedLength) {
			rewindedLength += length;
			return true;
		} else {
			return false;
		}
	}
	
	private void decreaseRewindedLength(double length) {
		if(rewindedLength != 0) {
			rewindedLength -= length;
			if(rewindedLength < 0)
				rewindedLength = 0;
		}
	}

	@Override
	public void setSpeed(int speed) {
		if(running) {
			if((speed < 0) != runningBack) {
				map.stopMove();
				runningBack = !runningBack;
				if(speed < 0) {
					route.removeLast();
					dc.dispose();
				} else {
					route.add(car.getWay());
					dc.initialize(new CrossroadNode(car.getWay()));
				}
				
				if(!car.isOnCrossroad()) {
					map.moveTo(
							speed < 0 ? car.moveBackAndReturnPoint() : car.moveAndReturnPoint(), 
							this);
				} else {
					mapMoveFinished();
				}
			}
			map.setSpeed(Math.abs(speed));
		}
	}
}
