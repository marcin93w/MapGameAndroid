package com.mapgame.engine;

import java.util.ArrayList;

import jsqlite.Exception;

import org.json.JSONException;

import com.mapgame.mapprojection.Map;
import com.mapgame.mapprojection.MapMenageable;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.streetsgraph.CarPosition;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.Way;
import com.mapgame.turnsensor.Turnable;

public class Engine implements MapMenageable, Turnable {
	Map map;
	StreetsDataSource sds;
	ComponentsManager cm;

	CarPosition car;
	int turnAngle = 0;
	boolean stop = false;
	
	public Engine(Map map, StreetsDataSource ds, ComponentsManager cm) {
		this.map = map;
		this.sds = ds;
		this.cm = cm;
	}

	public void drive(Way startWay) {
		car = new CarPosition(startWay, false);
        map.setPosition(car.getPoint());
        map.moveTo(car.getNextPoint(), this);
	}
	
	public void drive() {
		try {
			drive(sds.getRandomWay());
		} catch (Exception e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		stop = true;
	}
	
	@Override
	public void turn(int degrees) {
		turnAngle = degrees;
		if(car != null)
		cm.drawDirectionArrow(car.getDirectionVector().rotate(degrees));
	}

	@Override
	public void mapMoveFinished() {
		if(!stop)
			continueDrive();
	}

	private void continueDrive() {
        if(!car.isOnCrossroad()) {
            map.moveTo(car.getNextPoint(), this);
            cm.drawDirectionArrow(car.getDirectionVector().rotate(turnAngle));
        } else {
        	ArrayList<CarPosition> crossroadPositions = null;
            try {
				crossroadPositions = sds.getPossiblePositionsFromCrossroad(car.getCrossroadNode());
			} catch (Exception e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
            
            CrossroadSolver crossroad = new CrossroadSolver(car.getPoint(), 
            		car.getPrevPoint(),
            		turnAngle);
            
            for(CarPosition possiblePosition : crossroadPositions) {
            	crossroad.addArm(possiblePosition);
            }
            
            car = crossroad.getNextPosition();
            continueDrive();   
        }
		
	}
}
