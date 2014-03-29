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
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.mapgame.arrowsturn.ArrowsTurnCrossroadSolverFactory;
import com.mapgame.arrowsturn.TurnArrows;
import com.mapgame.engine.CrossroadSolverFactory;
import com.mapgame.engine.Engine;
import com.mapgame.mapprojection.Map;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.sensorturn.TurnSensor;
import com.mapgame.streetsgraph.StreetsDataSource;

public class MainActivity extends Activity {

	private Map map;
	private Engine engine;
	private StreetsDataSource ds;
	private ComponentsManager cm;
	private CrossroadSolverFactory csf;

	private SensorManager sensorManager;
	private TurnSensor turnSensor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initializeMap();
		cm = new ComponentsManager(this);
		// initializeDirectionArrowSurfaceView();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		onDASurfaceCreated();
	}

	private void initializeMap() {
		MapView myOpenMapView = (MapView) findViewById(R.id.mapview);
		myOpenMapView.setBuiltInZoomControls(false);
		myOpenMapView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		map = new Map((MapController) myOpenMapView.getController(), this);
	}

	@SuppressWarnings("unused")
	private void initializeDirectionArrowSurfaceView() {
		SurfaceView directionArrowSurface = (SurfaceView) findViewById(R.id.surfaceView1);
		directionArrowSurface.setZOrderOnTop(true);
		directionArrowSurface.setVisibility(View.VISIBLE);
		SurfaceHolder daSufraceHolder = directionArrowSurface.getHolder();
		daSufraceHolder.setFormat(PixelFormat.TRANSPARENT);
		daSufraceHolder.addCallback(new SurfaceHolder.Callback() {
			public void surfaceDestroyed(SurfaceHolder holder) {
				onDASurfaceDestroyed();
			}

			public void surfaceCreated(SurfaceHolder holder) {
				cm.setDirectionArrowSurfaceHolder(holder);
				onDASurfaceCreated();
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});
	}

	public void onDASurfaceCreated() {
		// turn Arrows
		TurnArrows ta = new TurnArrows(this,
				(RelativeLayout) findViewById(R.id.buttonsPanel));

		// database init
		ds = new StreetsDataSource();

		csf = new ArrowsTurnCrossroadSolverFactory(ds, ta);

		/*
		 * /turn sensor init turnSensor = new
		 * TurnSensor((SensorTurnCrossroadSolverFactory)csf);
		 * sensorManager.registerListener(turnSensor,
		 * sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		 * SensorManager.SENSOR_DELAY_NORMAL); /
		 */

		// engine start
		engine = new Engine(map, ds, cm, csf);
		engine.drive();
	}

	public void onDASurfaceDestroyed() {
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
		// TODO pause game onPause
		sensorManager.registerListener(turnSensor,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// TODO pause game onPause
		super.onPause();
		sensorManager.unregisterListener(turnSensor);
	}

}
