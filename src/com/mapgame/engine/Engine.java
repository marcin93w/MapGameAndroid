package com.mapgame.engine;

import com.mapgame.mapprojection.Map;
import com.mapgame.mapprojection.MapMenageable;
import com.mapgame.streetsgraph.Way;

public class Engine implements MapMenageable {
	Map map;

	Way currentWay;
	int currentPointId;
	boolean backward;
	
	public Engine(Map map) {
		this.map = map;
	}

	public void drive(Way startWay) {
		currentWay = startWay;
        currentPointId = 0;
        backward = false;
        map.setPosition(currentWay.getPoints().get(currentPointId));
        map.moveTo(currentWay.getPoints().get(++currentPointId), this);
	}

	@Override
	public void mapMoveFinished() {
		continueDrive();
	}

	private void continueDrive() {
		int step = backward ? -1 : 1;
        if(!((backward && currentPointId == 0) || (!backward && currentPointId == currentWay.getPoints().size()-1))) {
        	//node is not a crossroad
            map.moveTo(currentWay.getPoints().get(currentPointId += step), this);
        } else {
            //node is a crossroad
            if(currentWay.getNextForward() != null && !currentWay.getNextForward().isEmpty()) {
            	currentWay = currentWay.getNextForward().get(0);
            	backward = false;
            	currentPointId = 0;
            	continueDrive();
            }
        	
        	/*var crossroad = new CrossroadSolver(currentWay.points[currentPointId], currentWay.points[currentPointId - step]);
            
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
