package com.mapgame.sensorturn;

import java.util.ArrayList;

import com.mapgame.engine.CarPosition;
import com.mapgame.engine.CrossroadSolver;
import com.mapgame.engine.DirectionVector;
import com.mapgame.streetsgraph.Point;

public class SensorTurnCrossroadSolver extends CrossroadSolver {
	
	public SensorTurnCrossroadSolver(Point center, Point previous, int turnDegrees) {
		super((new DirectionVector(previous, center)).rotate(turnDegrees),
				new ArrayList<CarPosition>());
	}
	
	public SensorTurnCrossroadSolver(Point center, Point previous, 
			ArrayList<CarPosition> arms, int turnDegrees) {
		super((new DirectionVector(previous, center)).rotate(turnDegrees),
				arms);
	}
	
	public CarPosition getNextPosition() {
	    return getArmClosestToDirectionVector();
	};
}
