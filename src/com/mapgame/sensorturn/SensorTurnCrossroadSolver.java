package com.mapgame.sensorturn;

import java.util.ArrayList;

import com.mapgame.engine.CrossroadSolver;
import com.mapgame.engine.DirectionVector;
import com.mapgame.streetsgraph.Road;

public class SensorTurnCrossroadSolver extends CrossroadSolver {

	public SensorTurnCrossroadSolver(DirectionVector vector,
			ArrayList<Road> arms, int turnDegrees) {
		super(vector.rotate(turnDegrees), arms);
	}

	public Road getNextRoad() {
		return getArmClosestToDirectionVector();
	};
}
