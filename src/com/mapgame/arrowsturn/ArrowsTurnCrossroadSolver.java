package com.mapgame.arrowsturn;

import java.util.ArrayList;

import com.mapgame.engine.CarPosition;
import com.mapgame.engine.CrossroadSolver;
import com.mapgame.streetsgraph.Point;

public class ArrowsTurnCrossroadSolver extends CrossroadSolver {

	protected ArrowsTurnCrossroadSolver(Point center, Point previous,
			ArrayList<CarPosition> arms) {
		super(center, previous, arms);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CarPosition getNextPosition() {
		return getArmClosestToDirectionVector();
	}

}
