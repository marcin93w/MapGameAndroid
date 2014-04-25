package com.mapgame;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import com.mapgame.streetsgraph.model.Point;

public abstract class RacePreviewActivity extends MapActivity {
	
	
	@Override
	protected void mapLayoutInitialize() {
		loadLayout();
		setMap((MapView) findViewById(R.id.mappreview));
	}
	
	protected abstract void loadLayout();
	
	
	
	public void addStartFlagToMap(Point start) {
		drawFlagOnMap(R.drawable.start_flag, new OverlayItem("Start", "", start));
	}

	public void addEndFlagToMap(Point end) {
		drawFlagOnMap(R.drawable.end_flag, new OverlayItem("Destination", "", end));
	}
	
	public void endPreview() {
		finish();
	}
	
	public void invokeEndPreview() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				endPreview();
			}
		});
	}
	
}
