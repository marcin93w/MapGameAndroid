package com.mapgame;

import org.json.JSONException;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.mapgame.engine.Engine;
import com.mapgame.mapprojection.Map;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.Way;

public class MainActivity extends Activity {

	private Map map;
	private Engine engine;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    MapView myOpenMapView = (MapView)findViewById(R.id.mapview);
	    myOpenMapView.setBuiltInZoomControls(false);
	    myOpenMapView.setClickable(false);
	    map = new Map((MapController)myOpenMapView.getController(), this);
	    
	    StreetsDataSource ds = new StreetsDataSource();
	    engine = new Engine(map, ds);
	    
	    try {
			Way way = ds.getRandomWay();
			if(way == null)
				Toast.makeText(getApplicationContext(), "empty :(", Toast.LENGTH_LONG).show();
			else {
				Toast.makeText(getApplicationContext(), "success :)", Toast.LENGTH_LONG).show();
				engine.drive(way);
			}
			
		} catch (jsqlite.Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	    
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
