package com.mapgame.engine;

import org.json.JSONException;

import com.mapgame.arrowsturn.DrivingController;
import com.mapgame.mapprojection.GameMap;
import com.mapgame.mapprojection.MapMoveMenageable;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.streetsgraph.Way;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.Road;

public class Engine implements MapMoveMenageable {
	GameMap map;
	StreetsDataSource sds;
	ComponentsManager cm;
	DrivingController dc;

	Car car;
	int turnAngle = 0;
	boolean stop = false;

	public Engine(GameMap map, StreetsDataSource ds, ComponentsManager cm,
			DrivingController dc) {
		this.map = map;
		this.sds = ds;
		this.cm = cm;
		this.dc = dc;
	}

	public void drive(Road startWay) {
		car = new Car(new Way(startWay, false));
		map.setPosition(car.getPoint());
		map.moveTo(car.moveAndReturnPoint(), this);
		dc.initialize(car.getRoad());
	}

	public void drive() {
		try {
			drive(sds.getWay("Wielicka"));
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		stop = true;
	}
	
	public void start() {
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
			cm.drawCar(car.getRoad().createDirectionVector(car.pointIdx));
		} else {
			cm.updateCounters(car.getRoad().getRoad().getLength(), 
					car.getRoad().getRoad().getCost());

			car.setRoad(dc.getNextRoad());
			continueDrive();
		}

	}
}
