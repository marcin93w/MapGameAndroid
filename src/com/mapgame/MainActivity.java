package com.mapgame;

import java.util.ArrayList;

import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import com.mapgame.streetsgraph.DataLoadException;
import com.mapgame.streetsgraph.DataLoader;
import com.mapgame.streetsgraph.Way;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MapView myOpenMapView;
	private MapController myMapController;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    myOpenMapView = (MapView)findViewById(R.id.mapview);
	    myOpenMapView.setBuiltInZoomControls(true);
	    myMapController = (MapController) myOpenMapView.getController();
	    myMapController.setZoom(4);
	    
	    DataLoader dl = new DataLoader();
	    try {
			ArrayList<Way> ways = (ArrayList<Way>) dl.loadData();
			if(ways.isEmpty())
				Toast.makeText(getApplicationContext(), "empty :(", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getApplicationContext(), "success :)", Toast.LENGTH_LONG).show();
			
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
