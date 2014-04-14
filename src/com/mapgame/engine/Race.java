package com.mapgame.engine;

import org.json.JSONException;

import com.mapgame.mapprojection.gamemap.GameMap;
import com.mapgame.mapprojection.gamemap.GameMapCallback;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.model.Car;
import com.mapgame.streetsgraph.model.Road;
import com.mapgame.streetsgraph.model.Way;

public class Race implements GameMapCallback {
	GameMap map;
	StreetsDataSource sds;
	ComponentsManager cm;
	DrivingController dc;

	Car car;
	int turnAngle = 0;
	boolean stop = false;

	public Race(GameMap map, StreetsDataSource ds, ComponentsManager cm,
			DrivingController dc) {
		this.map = map;
		this.sds = ds;
		this.cm = cm;
		this.dc = dc;
	}

	public void start(Road startWay) {
		car = new Car(new Way(startWay, false));
		map.setPosition(car.getPoint());
		map.moveTo(car.moveAndReturnPoint(), this);
		dc.initialize(car.getRoad());
	}

	public void start() {
		try {
			start(sds.getWay("Wielicka"));
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
			cm.drawCar(car.getRoad().createDirectionVector(car.getPointIdx()));
		} else {
			//cm.updateCounters(car.getRoad().getRoad().getLength(), 
			//		car.getRoad().getRoad().getCost());

			car.setRoad(dc.getNextRoad());
			continueDrive();
		}

	}
}
