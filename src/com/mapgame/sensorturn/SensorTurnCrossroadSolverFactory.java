package com.mapgame.sensorturn;

import com.mapgame.engine.CarPosition;
import com.mapgame.engine.CrossroadSolver;
import com.mapgame.engine.CrossroadSolverFactory;
import com.mapgame.streetsgraph.StreetsDataSource;

public class SensorTurnCrossroadSolverFactory extends CrossroadSolverFactory
		implements SensorTurnable {

	private int turnAngle = 0;

	public SensorTurnCrossroadSolverFactory(StreetsDataSource sds) {
		super(sds);
	}

	@Override
	public CrossroadSolver getCrossroadSolver(CarPosition car) {
		return new SensorTurnCrossroadSolver(car.getPoint(),
				car.getPrevPoint(), getCrossroadArms(car.getCrossroadNode()),
				turnAngle);
	}

	@Override
	public void turn(int degrees) {
		turnAngle = degrees;
	}

}
