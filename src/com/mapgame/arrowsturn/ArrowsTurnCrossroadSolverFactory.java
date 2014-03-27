package com.mapgame.arrowsturn;

import com.mapgame.engine.CarPosition;
import com.mapgame.engine.CrossroadSolver;
import com.mapgame.engine.CrossroadSolverFactory;
import com.mapgame.streetsgraph.StreetsDataSource;

public class ArrowsTurnCrossroadSolverFactory extends CrossroadSolverFactory {

	TurnArrows arrowsManager;
	
	public ArrowsTurnCrossroadSolverFactory(StreetsDataSource sds, TurnArrows ta) {
		super(sds);
		arrowsManager = ta;
	}

	@Override
	public CrossroadSolver getCrossroadSolver(CarPosition position) {
		arrowsManager.setArrow(position.getDirectionVector());
		return new ArrowsTurnCrossroadSolver(position.getPoint(), position.getPrevPoint(), 
				getCrossroadArms(position.getCrossroadNode()));
	}

}
