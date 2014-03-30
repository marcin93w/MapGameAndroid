package com.mapgame.engine;

import java.util.ArrayList;

import com.mapgame.streetsgraph.Point;
import com.mapgame.streetsgraph.Road;

public abstract class CrossroadSolver {
	DirectionVector directionVector;
	protected ArrayList<Road> arms;

	protected CrossroadSolver(Point center, Point previous,
			ArrayList<Road> arms) {
		directionVector = (new DirectionVector(previous, center));
		this.arms = arms;
	}

	protected CrossroadSolver(DirectionVector directionVector,
			ArrayList<Road> arms) {
		this.directionVector = directionVector;
		this.arms = arms;
	}

	public abstract Road getNextRoad();

	protected Road getArmClosestToDirectionVector() {
		return arms.get(getIndexOfArmClosestToDirectionVector());
	}
	
	protected int getIndexOfArmClosestToDirectionVector() {
		double angle = Double.MAX_VALUE;
		int idx = 0;

		for (int i = 0; i < this.arms.size(); i++) {
			double calculatedAngle = arms.get(i)
					.createDirectionVector(Road.Position.START)
					.getAbsAngle(this.directionVector);
			if (calculatedAngle < angle) {
				angle = calculatedAngle;
				idx = i;
			}
		}

		return idx;
	}

}
