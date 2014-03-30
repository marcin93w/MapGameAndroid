package com.mapgame.arrowsturn;

import java.util.ArrayList;

import com.mapgame.engine.CrossroadSolver;
import com.mapgame.engine.DirectionVector;
import com.mapgame.streetsgraph.Road;

public class ArrowsTurnCrossroadSolver extends CrossroadSolver {

	int chosenPositionId;
	
	ArrayList<ArrowsTurnCrossroadSolver> childCrossroads;
	
	protected ArrowsTurnCrossroadSolver(DirectionVector vector,
			ArrayList<Road> arms) {
		super(vector, arms);
		chosenPositionId = getIndexOfArmClosestToDirectionVector();
		childCrossroads = new ArrayList<ArrowsTurnCrossroadSolver>(arms.size());
		for(int i=0; i<arms.size(); i++)
			childCrossroads.add(null);
	}

	public Road getChosenRoad() {
		return arms.get(chosenPositionId);
	}

	public void setChosenRoad(Road chosenRoad) {
		setChosenRoad(chosenRoad, null);
	}
	
	public void setChosenRoad(Road chosenRoad, 
			Road childChoosenRoad) {
		this.chosenPositionId = arms.indexOf(chosenRoad);
		if(childChoosenRoad != null)
			childCrossroads.get(chosenPositionId).setChosenRoad(childChoosenRoad);
	}

	public void setChildCrossroad (Road arm, ArrowsTurnCrossroadSolver crossroad) {
		childCrossroads.set(arms.indexOf(arm), crossroad);
	}
	
	public ArrowsTurnCrossroadSolver getNextCrossroad() {
		return childCrossroads.get(chosenPositionId);
	}

	@Override
	public Road getNextRoad() {
		return getChosenRoad();
	}

}
