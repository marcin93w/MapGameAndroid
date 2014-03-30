package com.mapgame.engine;

import java.util.ArrayList;

import org.json.JSONException;

import com.mapgame.streetsgraph.Road;
import com.mapgame.streetsgraph.StreetsDataSource;

public abstract class CrossroadSolverFactory {
	StreetsDataSource sds;

	public CrossroadSolverFactory(StreetsDataSource sds) {
		this.sds = sds;
	}

	public abstract CrossroadSolver getCrossroadSolver(Road position);

	protected ArrayList<Road> getCrossroadArms(int crossroadNodeId) {
		ArrayList<Road> crossroadRoads = null;
		try {
			crossroadRoads = sds
					.getPossibleRoadsFromCrossroad(crossroadNodeId);
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return crossroadRoads;
	}
}
