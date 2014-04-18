package com.mapgame;

import java.util.ArrayList;
import java.util.LinkedList;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mapgame.arrowsturn.ArrowsDisplayableActivity;
import com.mapgame.engine.Game;
import com.mapgame.mapprojection.MapViewManageableActivity;
import com.mapgame.streetsgraph.model.Point;

public class MainActivity extends Activity 
		implements MapViewManageableActivity, ArrowsDisplayableActivity {

	View pauseScreen;
	MapView gameMapView, previewMapView;
	View turnArrowsView;
	TextView nextStreetView;
	
	Game game;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_main);
		pauseScreen = findViewById(R.id.pauseScreen);
		resumeSetUp();
		turnArrowsView = (RelativeLayout) findViewById(R.id.buttonsPanel);
		nextStreetView = (TextView) findViewById(R.id.street);

		gameMapView = (MapView) findViewById(R.id.mapview);
		disableMapTouch(gameMapView);
		previewMapView = (MapView) findViewById(R.id.mappreview);
		disableMapTouch(previewMapView);
		
		game = new Game(this);
	}

	private void disableMapTouch(MapView map) {
		map.setBuiltInZoomControls(false);
		map.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}
	
	private void enableMapTouch(MapView map) {
		map.setBuiltInZoomControls(true);
		map.setOnTouchListener(new OnTouchListener() {		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
	}
	
	public void setOnSlowClickListener(OnCheckedChangeListener listener) {
		ToggleButton slowButton = (ToggleButton) findViewById(R.id.slowButton);
		slowButton.setOnCheckedChangeListener(listener);
	}

	public void initializeCarSurfaceView(SurfaceHolder.Callback callback) {
		SurfaceView carSurface = (SurfaceView) findViewById(R.id.surfaceView1);
		carSurface.setZOrderOnTop(true);
		carSurface.setVisibility(View.VISIBLE);
		SurfaceHolder carSufraceHolder = carSurface.getHolder();
		carSufraceHolder.setFormat(PixelFormat.TRANSPARENT);
		carSufraceHolder.addCallback(callback);
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
				game.unpause();		
				pauseScreen.setVisibility(View.GONE);
			}
		});
	}
	
	@Override
	protected void onPause() {
		game.pause();
		pauseScreen.setVisibility(View.VISIBLE);
		super.onPause();
	}

	
	//*************************MAP MANAGEABLE ACTIVITY METHODS**********************
	@Override
	public void invokeMapController(final MapControllerRunable job,
			final MapType mapType) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				switch(mapType) {
				case GAME_MAP:
					job.run(gameMapView.getController());
					break;
				case PREVIEW_MAP:
					job.run(previewMapView.getController());
				}
			}
		});
	}

	@Override
	public IMapController getController(MapType mapType) {
		switch(mapType) {
		case GAME_MAP:
			return gameMapView.getController();
		case PREVIEW_MAP:
			return previewMapView.getController();
		default:
			return null;
		}
	}

	@Override
	public void showPreviewMap() {
		showPreviewMap(false, null);
	}
	
	@Override
	public void showPreviewMap(final boolean enableOnTouchListeners,
			final OnClickListener nextGameButtonListener) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				View previewMapViewL = findViewById(R.id.mapprevievLayout);
				previewMapViewL.setVisibility(View.VISIBLE);
				if(enableOnTouchListeners)
					enableMapTouch(previewMapView);
				if(nextGameButtonListener != null) {
					View nextGameButton = findViewById(R.id.playagain);
					nextGameButton.setVisibility(View.VISIBLE);
					nextGameButton.setOnClickListener(nextGameButtonListener);
				}
			}
		});	
	}

	@Override
	public void hidePreviewMap(final boolean clearMap) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				View previewMapViewL = findViewById(R.id.mapprevievLayout);
				previewMapViewL.setVisibility(View.GONE);
				disableMapTouch(previewMapView);
				View nextGameButton = findViewById(R.id.playagain);
				nextGameButton.setVisibility(View.GONE);
				if(clearMap) {
					previewMapView.getOverlays().clear();
				}
			}
		});
	}

	@Override
	public void addStartFlagToMap(OverlayItem startFlag, MapType mapType) {
		drawFlagOnMap(R.drawable.start_flag, startFlag, mapType);
	}

	@Override
	public void addEndFlagToMap(OverlayItem endFlag, MapType mapType) {
		drawFlagOnMap(R.drawable.end_flag, endFlag, mapType);
	}
	
	private void drawFlagOnMap(int flagId, OverlayItem item, MapType mapType) {
		final ArrayList<OverlayItem> itemList = new ArrayList<OverlayItem>();
		itemList.add(item);
		ItemizedIconOverlay<OverlayItem> itemLocationOverlay = 
				new ItemizedIconOverlay<OverlayItem>(itemList, 
						getResources().getDrawable(flagId) ,null,
						new ResourceProxyImpl(this));
		MapView map = mapType == MapType.GAME_MAP ? gameMapView : previewMapView;
		map.getOverlays().add(itemLocationOverlay);
	}

	public void addPathToPreviewMap(final LinkedList<Point> path, final int color) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				PathOverlay po = new PathOverlay(color, getApplicationContext());
				po.addPoints(new LinkedList<IGeoPoint>(path));
				previewMapView.getOverlays().add(po);
			}
		});
	}
	
	//******************************************************************************

	//*************************ARROWS DRAWABLE ACTIVITY METHODS**********************
	@Override
	public void invokeArrowsView(final ViewRunnable job) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				job.run(turnArrowsView);
			}
		});
	}
	
	@Override
	public void invokeNextStreetView(final ViewRunnable job) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				job.run(nextStreetView);
			}
		});
	}

	@Override
	public TextView getStreetNameView() {
		return nextStreetView;
	}
	
	//******************************************************************************
	
}
