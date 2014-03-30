package com.mapgame.engine;

import org.json.JSONException;

import com.mapgame.arrowsturn.ArrowsTurnCrossroadSolverFactory;
import com.mapgame.mapprojection.Map;
import com.mapgame.mapprojection.MapMenageable;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.streetsgraph.Road;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.Way;

public class Engine implements MapMenageable {
	Map map;
	StreetsDataSource sds;
	ComponentsManager cm;
	CrossroadSolverFactory csf;

	Car car;
	int turnAngle = 0;
	boolean stop = false;

	public Engine(Map map, StreetsDataSource ds, ComponentsManager cm,
			CrossroadSolverFactory csf) {
		this.map = map;
		this.sds = ds;
		this.cm = cm;
		this.csf = csf;
	}

	public void drive(Way startWay) {
		car = new Car(new Road(startWay, false));
		map.setPosition(car.getPoint());
		map.moveTo(car.move(), this);
		((ArrowsTurnCrossroadSolverFactory) csf).initialize(car.getRoad());
	}

	public void drive() {
		try {
			drive(sds.getRandomWay());
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		stop = true;
	}

	@Override
	public void mapMoveFinished() {
		if (!stop)
			continueDrive();
	}

	private void continueDrive() {
		if (!car.isOnCrossroad()) {
			map.moveTo(car.move(), this);
		} else {
			cm.updateCounters(car.getRoad().getWay().getLength(), 
					car.getRoad().getWay().getCost());
			CrossroadSolver crossroad = csf.getCrossroadSolver(car.getRoad());

			car.setRoad(crossroad.getNextRoad());
			continueDrive();
		}

	}
}
