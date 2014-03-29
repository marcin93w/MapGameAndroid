package com.mapgame.engine;

import java.util.ArrayList;

import org.json.JSONException;

import com.mapgame.streetsgraph.StreetsDataSource;

public abstract class CrossroadSolverFactory {
	StreetsDataSource sds;

	public CrossroadSolverFactory(StreetsDataSource sds) {
		this.sds = sds;
	}

	public abstract CrossroadSolver getCrossroadSolver(CarPosition position);

	protected ArrayList<CarPosition> getCrossroadArms(int crossroadNodeId) {
		ArrayList<CarPosition> crossroadPositions = null;
		try {
			crossroadPositions = sds
					.getPossiblePositionsFromCrossroad(crossroadNodeId);
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return crossroadPositions;
	}
}
