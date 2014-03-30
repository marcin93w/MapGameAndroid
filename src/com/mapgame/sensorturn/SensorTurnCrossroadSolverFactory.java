package com.mapgame.sensorturn;

import com.mapgame.engine.CrossroadSolver;
import com.mapgame.engine.CrossroadSolverFactory;
import com.mapgame.streetsgraph.Road;
import com.mapgame.streetsgraph.StreetsDataSource;

public class SensorTurnCrossroadSolverFactory extends CrossroadSolverFactory
		implements SensorTurnable {

	private int turnAngle = 0;

	public SensorTurnCrossroadSolverFactory(StreetsDataSource sds) {
		super(sds);
	}

	@Override
	public CrossroadSolver getCrossroadSolver(Road road) {
		return new SensorTurnCrossroadSolver(
				road.createDirectionVector(Road.Position.END), 
				getCrossroadArms(road.getEndCrossroadNode()),
				turnAngle);
	}

	@Override
	public void turn(int degrees) {
		turnAngle = degrees;
	}

}
