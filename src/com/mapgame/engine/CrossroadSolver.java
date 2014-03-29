package com.mapgame.engine;

import java.util.ArrayList;

import com.mapgame.streetsgraph.Point;

public abstract class CrossroadSolver {
	DirectionVector directionVector;
	ArrayList<CarPosition> arms;

	protected CrossroadSolver(Point center, Point previous,
			ArrayList<CarPosition> arms) {
		directionVector = (new DirectionVector(previous, center));
		this.arms = arms;
	}

	protected CrossroadSolver(DirectionVector directionVector,
			ArrayList<CarPosition> arms) {
		this.directionVector = directionVector;
		this.arms = arms;
	}

	public abstract CarPosition getNextPosition();

	protected CarPosition getArmClosestToDirectionVector() {
		double angle = Double.MAX_VALUE;
		CarPosition arm = arms.get(0);

		for (int i = 0; i < this.arms.size(); i++) {
			double calculatedAngle = arms.get(i).getDirectionVector()
					.getAbsAngle(this.directionVector);
			if (calculatedAngle < angle) {
				angle = calculatedAngle;
				arm = this.arms.get(i);
			}
		}

		return arm;
	}

}
