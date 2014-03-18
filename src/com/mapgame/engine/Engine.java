package com.mapgame.engine;

import java.util.ArrayList;
import java.util.Random;

import jsqlite.Exception;

import org.json.JSONException;

import com.mapgame.mapprojection.Map;
import com.mapgame.mapprojection.MapMenageable;
import com.mapgame.streetsgraph.CarPosition;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.Way;

public class Engine implements MapMenageable {
	Map map;
	StreetsDataSource sds;

	CarPosition car;
	
	public Engine(Map map, StreetsDataSource ds) {
		this.map = map;
		this.sds = ds;
	}

	public void drive(Way startWay) {
		car = new CarPosition(startWay, false);
        map.setPosition(car.getPoint());
        map.moveTo(car.getNextPoint(), this);
	}

	@Override
	public void mapMoveFinished() {
		continueDrive();
	}

	private void continueDrive() {
        if(!car.isOnCrossroad()) {
            map.moveTo(car.getNextPoint(), this);
        } else {
        	ArrayList<CarPosition> crossroadPositions = null;
            try {
				crossroadPositions = sds.getPossiblePositionsFromCrossroad(car.getCrossroadNode());
			} catch (Exception e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
            
            Random rand = new Random();
            car = crossroadPositions.get(rand.nextInt(crossroadPositions.size()));
            continueDrive();
        	/*
        	var crossroad = new CrossroadSolver(currentWay.points[currentPointId], currentWay.points[currentPointId - step]);
            
            var turnAngle = getTurnAngle();
            crossroad.turn(turnAngle);
            if(turnAngle <= 90 || turnAngle >= 270)
                crossroad.canTurnAround = false;
            
            crossroad.addAllWaysAsArms(currentWay, backward);        
            var chosenArm = crossroad.getClosestArm();
            
            currentWay = chosenArm.way;
            backward = chosenArm.backward;
            currentPointId = backward ? currentWay.points.length - 1 : 0;
            continueDrive();*/         
        }
		
	}
}
