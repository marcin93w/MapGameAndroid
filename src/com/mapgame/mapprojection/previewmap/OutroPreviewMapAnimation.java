package com.mapgame.mapprojection.previewmap;

import java.util.LinkedList;

import com.mapgame.streetsgraph.model.Way;

/*
 * Not needed for now
 */
public class OutroPreviewMapAnimation extends Thread {
	PreviewMap map;
	PreviewMapCallback callback;
	
	LinkedList<Way> userRoute, bestRoute;

	public OutroPreviewMapAnimation(PreviewMap map,
			PreviewMapCallback callback, LinkedList<Way> userRoute,
			LinkedList<Way> bestRoute) {
		super();
		this.map = map;
		this.callback = callback;
		this.userRoute = userRoute;
		this.bestRoute = bestRoute;
	}
	
	@Override
	public void run() {
		
	}
}
