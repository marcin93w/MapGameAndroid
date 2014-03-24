package com.mapgame;

import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;

import com.mapgame.engine.Engine;
import com.mapgame.mapprojection.Map;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.turnsensor.TurnSensor;

public class MainActivity extends Activity {

	private Map map;
	private Engine engine;
	private StreetsDataSource ds;
	private GameSurface gameSurface;
	private SensorManager sensorManager;
	private TurnSensor turnSensor;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    //map params
	    MapView myOpenMapView = (MapView)findViewById(R.id.mapview);
	    myOpenMapView.setBuiltInZoomControls(false);
	    myOpenMapView.setOnTouchListener(new OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	    map = new Map((MapController)myOpenMapView.getController(), this);	
	    
	    //surfaceView params
	    gameSurface = (GameSurface)findViewById(R.id.surfaceView1);
	    gameSurface.setMainActivity(this);
	    gameSurface.setZOrderOnTop(true);
	    SurfaceHolder sfhTrackHolder = gameSurface.getHolder();
	    sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);  
	    
	    //sensor init
	    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);    
	}
	
	public void onGameSurfaceCreated() {
		//database init    
	    ds = new StreetsDataSource();
	    
	    //engine start
	    engine = new Engine(map, ds, gameSurface.getComponentsManager());
	    engine.drive();
	    
	    //turn sensor init
	    turnSensor = new TurnSensor(engine);
	    sensorManager.registerListener(turnSensor,
	            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	            SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void onGameSurfaceDestroyed() {
		engine.stop();
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
      super.onResume();
      // register this class as a listener for the orientation and
      // accelerometer sensors
      sensorManager.registerListener(turnSensor,
          sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
          SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
      // unregister listener
      super.onPause();
      sensorManager.unregisterListener(turnSensor);
    }
    
}
