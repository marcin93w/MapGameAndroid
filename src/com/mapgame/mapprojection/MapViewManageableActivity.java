package com.mapgame.mapprojection;

import org.osmdroid.api.IMapController;

public interface MapViewManageableActivity { 
	
	public interface MapControllerRunable {
		void run(IMapController mapController);
	}
	
	void invokeMapController(MapControllerRunable job);
	IMapController getController();
	
}
