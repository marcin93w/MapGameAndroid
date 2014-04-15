package com.mapgame.engine;

import java.util.LinkedList;

import com.mapgame.streetsgraph.model.Way;

public interface RaceFinishedCallback {
	void onRaceFinished(LinkedList<Way> route);
}
