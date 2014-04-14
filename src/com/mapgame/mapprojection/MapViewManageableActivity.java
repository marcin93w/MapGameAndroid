package com.mapgame.mapprojection;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.overlay.OverlayItem;

public interface MapViewManageableActivity {
	public enum MapType { GAME_MAP, PREVIEW_MAP }; 
	
	public interface MapControllerRunable {
		void run(IMapController mapController);
	}
	
	void invokeMapController(MapControllerRunable job, MapType mapType);
	IMapController getController(MapType mapType);
	
	void showPreviewMap();
	void hidePreviewMap();
	
	void addStartFlagToMap(OverlayItem startFlag, MapType mapType);
	void addEndFlagToMap(OverlayItem endFlag, MapType mapType);
}
