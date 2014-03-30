package com.mapgame.arrowsturn;

import java.util.ArrayList;

import com.mapgame.engine.CrossroadSolver;
import com.mapgame.engine.CrossroadSolverFactory;
import com.mapgame.streetsgraph.Road;
import com.mapgame.streetsgraph.StreetsDataSource;

public class ArrowsTurnCrossroadSolverFactory extends CrossroadSolverFactory {

	TurnArrows arrowsManager;
	
	ArrowsTurnCrossroadSolver nextCrossroad;
	
	final static int minArrowDisplayTimeCost = 10;
	final static int minArrowDisplayTimeLength = 100;
	
	public ArrowsTurnCrossroadSolverFactory(StreetsDataSource sds, TurnArrows ta) {
		super(sds);
		arrowsManager = ta;
	}
	
	public void initialize(Road position) {
		nextCrossroad = (ArrowsTurnCrossroadSolver)createCrossroadSolver(position);		
	}

	@Override
	public CrossroadSolver getCrossroadSolver(Road road) {
		ArrowsTurnCrossroadSolver thisCrossroad = nextCrossroad;
		nextCrossroad = thisCrossroad.getNextCrossroad();
		if(nextCrossroad == null)
			nextCrossroad = (ArrowsTurnCrossroadSolver) createCrossroadSolver(
					thisCrossroad.getNextRoad());
		return thisCrossroad;
	}
	
	private CrossroadSolver createCrossroadSolver(Road road) {
		ArrayList<Road> arms = getCrossroadArms(road.getEndCrossroadNode());
		ArrowsTurnCrossroadSolver crossroad = new ArrowsTurnCrossroadSolver(
				road.createDirectionVector(Road.Position.END), 
				arms);
		arrowsManager.setCrossroad(crossroad);
		for(Road arm : arms) {
			if(arm.getWay().getLength() > minArrowDisplayTimeLength) {
				arrowsManager.addArrow(arm, 
						crossroad.getChosenRoad() == arm ? true : false);
			} else {
				ArrayList<Road> childArms = getCrossroadArms(arm.getEndCrossroadNode());
				ArrowsTurnCrossroadSolver childCrossroad = new ArrowsTurnCrossroadSolver(
						road.createDirectionVector(Road.Position.END), childArms);
				crossroad.setChildCrossroad(arm, childCrossroad);
				for(Road childArm : childArms) {
					if(childArm.getEndCrossroadNode() != road.getEndCrossroadNode()) {
						boolean isMain = false;
						if(crossroad.getChosenRoad() == arm && 
								childCrossroad.getChosenRoad() == childArm)
							isMain = true;
						arrowsManager.addArrow(arm, childArm, isMain);
					}
				}
			}
			
		}
		return crossroad;
	}

}
