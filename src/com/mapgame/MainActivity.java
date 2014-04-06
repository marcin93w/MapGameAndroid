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
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);

		initializeMap();
		cm = new ComponentsManager(this);
		// initializeDirectionArrowSurfaceView();
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
		
		ToggleButton slowButton = (ToggleButton) findViewById(R.id.slowButton);
		slowButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				map.setSpeed(isChecked ? Map.MoveSpeed.SLOW : Map.MoveSpeed.FAST);
			}
		});
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
				(RelativeLayout) findViewById(R.id.buttonsPanel),
				(TextView) findViewById(R.id.street));

		// database init
		ds = new StreetsDataSource();

		dc = new DrivingController(ta, ds);

		// engine start
		engine = new Engine(map, ds, cm, dc);
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
	}

	@Override
	protected void onPause() {
		// TODO pause game onPause
		super.onPause();
	}

}
