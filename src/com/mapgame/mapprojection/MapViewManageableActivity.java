package com.mapgame.mapprojection;

import java.util.LinkedList;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.overlay.OverlayItem;

import android.view.View.OnClickListener;

import com.mapgame.streetsgraph.model.Point;

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
	void addPathToPreviewMap(LinkedList<Point> path, int color);
	
	void addMapOnClickListener(OnClickListener l);
}
