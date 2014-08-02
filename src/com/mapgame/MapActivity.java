package com.mapgame;
import java.util.ArrayList;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.mapgame.mapprojection.MapViewManageableActivity;


public abstract class MapActivity extends Activity implements MapViewManageableActivity {

	private MapView map;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				
		mapLayoutInitialize();
	}
	
	protected MapView getMap() {
		return map;
	}
	
	protected void setMap(MapView map) {
		this.map = map;
	}

	protected abstract void mapLayoutInitialize();

	@Override
	public void invokeMapController(final MapControllerRunable job) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				job.run(map.getController());
			}
		});
	}

	@Override
	public IMapController getController() {
		return map.getController();
	}
	
	protected void drawFlagOnMap(int flagId, OverlayItem item) {
		final ArrayList<OverlayItem> itemList = new ArrayList<OverlayItem>();
		itemList.add(item);
		ItemizedIconOverlay<OverlayItem> itemLocationOverlay = 
				new ItemizedIconOverlay<OverlayItem>(itemList, 
						getResources().getDrawable(flagId) ,null,
						new ResourceProxyImpl(this));
		map.getOverlays().add(itemLocationOverlay);
	}
	
	public void clearOverlays() {
		map.getOverlays().clear();
	}
	
}
