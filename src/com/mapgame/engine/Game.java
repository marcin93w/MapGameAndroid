package com.mapgame.engine;


import java.util.LinkedList;

import jsqlite.Exception;

import org.json.JSONException;

import com.mapgame.RaceActivity;
import com.mapgame.arrowsturn.TurnArrows;
import com.mapgame.mapprojection.gamemap.GameMap;
import com.mapgame.mapprojection.previewmap.PreviewMapCallback;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.overlaycomponents.GameComponentsCallback;
import com.mapgame.overlaycomponents.RaceCountdownAnimation.Callback;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.model.CrossroadNode;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Route;
import com.mapgame.streetsgraph.model.Way;

/*
 * Main game controller
 */
public class Game implements GameComponentsCallback, RaceFinishedCallback {
	GameMap gameMap;
	TurnArrows turnArrows;
	ComponentsManager componentsManager;
	StreetsDataSource sds;
	RaceActivity raceActivity;
	
	Race race;
	CrossroadNode startNode, endNode;
	
	public Game(RaceActivity gameActivity) {
		this.raceActivity = gameActivity;
		this.gameMap = new GameMap(gameActivity);
		this.turnArrows = new TurnArrows(gameActivity, gameActivity.getApplicationContext());
		this.componentsManager = new ComponentsManager(gameActivity.getResources(), this);
		this.sds = new StreetsDataSource();
		
		gameActivity.initializeCarSurfaceView(componentsManager);
	}
	
	public void startTheRace() {
		this.race = new Race(gameMap, componentsManager, 
				new DrivingEngine(turnArrows, sds), this);
		raceActivity.setOnGearChangedListener(race);
		
		try {
			startNode = sds.getRandomCrossroadNode();
			endNode = sds.getRandomCrossroadNode(startNode.getWay().getFirstPoint(), 0.01);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
		
		race.initialize(startNode, endNode);
		Point start = startNode.getWay().getFirstPoint();
		Point end = endNode.getCrossroadPoint();
		gameMap.setStartEndFlags(start, end);
		
		raceActivity.showRaceIntro(startNode, endNode, new PreviewMapCallback() {
			@Override
			public void onPreviewFinished() {
				raceActivity.startCountdown(new Callback() {
					@Override
					public void raceCountdownFinished() {
						race.start();
					}
				});	
			}
		});
		
	}
	
	@Override
	public void onRaceFinished(LinkedList<Way> route) {
		Route bestRoute = null;	
		try {
			bestRoute = sds.getShortestRoute(startNode, endNode);
			bestRoute.addWayToBegin(startNode.getWay());
		} catch (Exception e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		raceActivity.showRaceFinish(startNode.getWay().getFirstPoint(),
				endNode.getCrossroadPoint(), 
				new Route(route), bestRoute, new PreviewMapCallback() {
					@Override
					public void onPreviewFinished() {
						startTheRace();
					}
				});
	}
	
	public boolean pause() {
		return race.pause();
	}
	
	public void unpause() {
		race.unpause();
	}

	@Override
	public void gameComponentsCreated() {
		if(race == null)
			startTheRace();
	}

	@Override
	public void gameComponentsDestroyed() {
		pause();
	}
	
}
