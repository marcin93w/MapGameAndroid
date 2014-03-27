package com.mapgame.arrowsturn;

import java.util.ArrayList;

import com.mapgame.engine.CarPosition;
import com.mapgame.engine.CrossroadSolver;
import com.mapgame.engine.CrossroadSolverFactory;
import com.mapgame.streetsgraph.StreetsDataSource;

public class ArrowsTurnCrossroadSolverFactory extends CrossroadSolverFactory {

	TurnArrows arrowsManager;
	
	ArrowsTurnCrossroadSolver nextCrossroad;
	
	public ArrowsTurnCrossroadSolverFactory(StreetsDataSource sds, TurnArrows ta) {
		super(sds);
		arrowsManager = ta;
	}
	
	public void initialize(CarPosition position) {
		nextCrossroad = (ArrowsTurnCrossroadSolver)createCrossroadSolver(position);		
	}

	@Override
	public CrossroadSolver getCrossroadSolver(CarPosition position) {
		CrossroadSolver thisCrossroad = nextCrossroad;
		nextCrossroad = (ArrowsTurnCrossroadSolver) createCrossroadSolver(
				thisCrossroad.getNextPosition());
		return thisCrossroad;
	}
	
	private CrossroadSolver createCrossroadSolver(CarPosition position) {
		ArrayList<CarPosition> arms = getCrossroadArms(position.getCrossroadNode());
		ArrowsTurnCrossroadSolver crossroad = new ArrowsTurnCrossroadSolver(
				position.getDirectionVector(), 
				arms);
		arrowsManager.setCrossroad(crossroad);
		for(CarPosition arm : arms) {
			arrowsManager.setArrow(arm, 
					crossroad.getChosenPosition() == arm ? true : false);
		}
		return crossroad;
	}

}
