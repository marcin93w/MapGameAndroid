package com.mapgame.engine;


import com.mapgame.MainActivity;
import com.mapgame.arrowsturn.TurnArrows;
import com.mapgame.mapprojection.gamemap.GameMap;
import com.mapgame.mapprojection.previewmap.PreviewMap;
import com.mapgame.mapprojection.previewmap.PreviewMapCallback;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.overlaycomponents.GameComponentsCallback;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.model.Point;

/*
 * Main game controller
 */
public class Game implements GameComponentsCallback {
	GameMap gameMap;
	PreviewMap previewMap;
	TurnArrows turnArrows;
	ComponentsManager componentsManager;
	
	Race race;
	
	public Game(MainActivity gameActivity) {
		this.gameMap = new GameMap(gameActivity);
		this.previewMap = new PreviewMap(gameActivity, new Point(50.065404,19.949255));
		this.turnArrows = new TurnArrows(gameActivity, gameActivity.getApplicationContext());
		this.componentsManager = new ComponentsManager(gameActivity.getResources(), this);
		
		gameActivity.setOnSlowClickListener(gameMap);
		gameActivity.initializeCarSurfaceView(componentsManager);
	}
	
	public void startTheGame() {
		race.start();
		race.pause();
		Point start = race.car.getPoint();
		Point end = new Point(50.0, 20.0);
		previewMap.showIntroPreview(start, end, new PreviewMapCallback() {
			@Override
			public void previewFinished() {
				race.unpause();
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
		StreetsDataSource sds = new StreetsDataSource();
		this.race = new Race(gameMap, sds,
				componentsManager, new DrivingController(turnArrows, sds));
		startTheGame();
	}

	@Override
	public void gameComponentsDestroyed() {
		pause();
	}
	
}
