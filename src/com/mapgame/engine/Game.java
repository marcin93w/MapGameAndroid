package com.mapgame.engine;


import com.mapgame.mapprojection.PreviewMap;
import com.mapgame.mapprojection.PreviewMapManageable;
import com.mapgame.streetsgraph.Point;

public class Game {
	Engine carEngine;
	PreviewMap previewMap;
	
	public Game(Engine carEngine, PreviewMap previewMap) {
		this.carEngine = carEngine;
		this.previewMap = previewMap;
	}
	
	public void startTheGame() {
		carEngine.drive();
		carEngine.stop();
		Point start = carEngine.car.getPoint();
		Point end = new Point(50.0, 20.0);
		previewMap.showPreview(start, end, new PreviewMapManageable() {
			@Override
			public void previewFinished() {
				carEngine.start();
			}
		});
		
	}
	
}
