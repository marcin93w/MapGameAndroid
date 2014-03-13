package com.mapgame;

import java.util.ArrayList;

import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.mapgame.engine.Engine;
import com.mapgame.mapprojection.Map;
import com.mapgame.streetsgraph.DataLoadException;
import com.mapgame.streetsgraph.DataLoader;
import com.mapgame.streetsgraph.Way;

public class MainActivity extends Activity {

	private Map map;
	private Engine engine;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    MapView myOpenMapView = (MapView)findViewById(R.id.mapview);
	    myOpenMapView.setBuiltInZoomControls(true);
	    myOpenMapView.setClickable(true);
	    map = new Map((MapController)myOpenMapView.getController());
	    engine = new Engine(map);
	    
	    DataLoader dl = new DataLoader();
	    try {
			ArrayList<Way> ways = (ArrayList<Way>) dl.loadData();
			if(ways.isEmpty())
				Toast.makeText(getApplicationContext(), "empty :(", Toast.LENGTH_LONG).show();
			else {
				Toast.makeText(getApplicationContext(), "success :)", Toast.LENGTH_LONG).show();
				engine.drive(ways.get(0));
			}
			
		} catch (DataLoadException e) {
			Toast.makeText(getApplicationContext(), e.getInnerException().getMessage(), Toast.LENGTH_LONG).show();
		}
	    
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
