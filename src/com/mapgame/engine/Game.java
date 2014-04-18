package com.mapgame.engine;


import java.util.LinkedList;

import jsqlite.Exception;

import org.json.JSONException;

import com.mapgame.MainActivity;
import com.mapgame.arrowsturn.TurnArrows;
import com.mapgame.mapprojection.gamemap.GameMap;
import com.mapgame.mapprojection.previewmap.PreviewMap;
import com.mapgame.mapprojection.previewmap.PreviewMapCallback;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.overlaycomponents.GameComponentsCallback;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.model.CrossroadNode;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Way;

/*
 * Main game controller
 */
public class Game implements GameComponentsCallback, RaceFinishedCallback {
	GameMap gameMap;
	PreviewMap previewMap;
	TurnArrows turnArrows;
	ComponentsManager componentsManager;
	StreetsDataSource sds;
	
	Race race;
	CrossroadNode startNode, endNode;
	
	public Game(MainActivity gameActivity) {
		this.gameMap = new GameMap(gameActivity);
		this.previewMap = new PreviewMap(gameActivity, new Point(50.065404,19.949255));
		this.turnArrows = new TurnArrows(gameActivity, gameActivity.getApplicationContext());
		this.componentsManager = new ComponentsManager(gameActivity.getResources(), this);
		this.sds = new StreetsDataSource();
		
		gameActivity.setOnSlowClickListener(gameMap);
		gameActivity.initializeCarSurfaceView(componentsManager);
	}
	
	public void startTheGame() {
		this.race = new Race(gameMap, componentsManager, 
				new DrivingController(turnArrows, sds), this);
		
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
		
		
		Point start = startNode.getWay().getFirstPoint();
		Point end = endNode.getCrossroadPoint();
		gameMap.setStartEnd(start, end);
		previewMap.showIntroPreview(start, end, new PreviewMapCallback() {
			@Override
			public void onPreviewFinished() {
				race.start(startNode, endNode);
			}
		});
		
	}
	
	@Override
	public void onRaceFinished(LinkedList<Way> route) {
		LinkedList<Point> bestRoute = null;
		try {
			bestRoute = sds.getShortestRoute(startNode, endNode);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		previewMap.showOutroPreview(route, bestRoute, new PreviewMapCallback() {
			@Override
			public void onPreviewFinished() {
				startTheGame();
			}
		});		
	}
	
	public void pause() {
		race.pause();
	}
	
	public void unpause() {
		race.unpause();
	}

	@Override
	public void gameComponentsCreated() {		
		startTheGame();
	}

	@Override
	public void gameComponentsDestroyed() {
		pause();
	}
	
}
