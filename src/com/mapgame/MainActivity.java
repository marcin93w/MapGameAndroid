package com.mapgame;

import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mapgame.arrowsturn.DrivingController;
import com.mapgame.arrowsturn.TurnArrows;
import com.mapgame.engine.Engine;
import com.mapgame.mapprojection.Map;
import com.mapgame.overlaycomponents.ComponentsManager;
import com.mapgame.streetsgraph.StreetsDataSource;

public class MainActivity extends Activity {

	private Map map;
	private Engine engine;
	private StreetsDataSource ds;
	private ComponentsManager cm;
	private DrivingController dc;

	View pauseScreen;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_main);
		pauseScreen = findViewById(R.id.pauseScreen);
		resumeSetUp();

		initializeMap();
		cm = new ComponentsManager(this);
		initializeCarSurfaceView();
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
		
		ToggleButton slowButton = (ToggleButton) findViewById(R.id.slowButton);
		slowButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				map.setSpeed(isChecked ? Map.MoveSpeed.SLOW : Map.MoveSpeed.FAST);
			}
		});
	}

	private void initializeCarSurfaceView() {
		SurfaceView carSurface = (SurfaceView) findViewById(R.id.surfaceView1);
		carSurface.setZOrderOnTop(true);
		carSurface.setVisibility(View.VISIBLE);
		SurfaceHolder carSufraceHolder = carSurface.getHolder();
		carSufraceHolder.setFormat(PixelFormat.TRANSPARENT);
		carSufraceHolder.addCallback(new SurfaceHolder.Callback() {
			public void surfaceDestroyed(SurfaceHolder holder) {
				onCarSurfaceDestroyed();
			}

			public void surfaceCreated(SurfaceHolder holder) {
				cm.setCarSurfaceHolder(holder, getResources());
				onCarSurfaceCreated();
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});
	}

	public void onCarSurfaceCreated() {
		// turn Arrows
		TurnArrows ta = new TurnArrows(this,
				(RelativeLayout) findViewById(R.id.buttonsPanel),
				(TextView) findViewById(R.id.street));

		// database init
		ds = new StreetsDataSource();

		dc = new DrivingController(ta, ds);

		// engine start
		engine = new Engine(map, ds, cm, dc);
		engine.drive();
	}

	public void onCarSurfaceDestroyed() {
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
	}

	private void resumeSetUp() {
		pauseScreen.setVisibility(View.GONE);
		Button resume = (Button) findViewById(R.id.unpause);
		resume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				engine.start();		
				pauseScreen.setVisibility(View.GONE);
			}
		});
	}
	
	@Override
	protected void onPause() {
		engine.stop();
		pauseScreen.setVisibility(View.VISIBLE);
		super.onPause();
	}

}
