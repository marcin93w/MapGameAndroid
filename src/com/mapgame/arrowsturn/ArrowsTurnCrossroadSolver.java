package com.mapgame.arrowsturn;

import java.util.ArrayList;

import com.mapgame.engine.CarPosition;
import com.mapgame.engine.CrossroadSolver;
import com.mapgame.engine.DirectionVector;

public class ArrowsTurnCrossroadSolver extends CrossroadSolver {

	CarPosition chosenPosition;
	
	protected ArrowsTurnCrossroadSolver(DirectionVector vector,
			ArrayList<CarPosition> arms) {
		super(vector, arms);
		chosenPosition = getArmClosestToDirectionVector();
	}

	public CarPosition getChosenPosition() {
		return chosenPosition;
	}

	public void setChosenPosition(CarPosition chosenPosition) {
		this.chosenPosition = chosenPosition;
	}

	@Override
	public CarPosition getNextPosition() {
		return chosenPosition;
	}

}
