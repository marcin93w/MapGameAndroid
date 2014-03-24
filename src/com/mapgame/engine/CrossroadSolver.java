package com.mapgame.engine;

import java.util.ArrayList;

import com.mapgame.streetsgraph.CarPosition;
import com.mapgame.streetsgraph.DirectionVector;
import com.mapgame.streetsgraph.Point;

public class CrossroadSolver {
	Point center;
	DirectionVector directionVector;
	ArrayList<CarPosition> arms;
	
	public CrossroadSolver(Point center, Point previous, int turnDegrees) {
		directionVector = (new DirectionVector(previous, center)).rotate(turnDegrees);
		arms = new ArrayList<CarPosition>();
	}
	
	public void addArm(CarPosition arm) {
		arms.add(arm);
	}
	
	public CarPosition getNextPosition() {
	    double angle = Double.MAX_VALUE;
	    CarPosition arm = arms.get(0);
	    
	    for(int i=0; i<this.arms.size(); i++) {
	        double calculatedAngle = arms.get(i).getDirectionVector()
	        		.getAngle(this.directionVector);
	        if(calculatedAngle < angle) {
	            angle = calculatedAngle;
	            arm = this.arms.get(i);
	        }
	    }
	    
	    return arm;
	};
}
