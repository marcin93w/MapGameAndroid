package com.mapgame;

import java.util.LinkedList;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mapgame.arrowsturn.ArrowsDisplayableActivity;
import com.mapgame.engine.Game;
import com.mapgame.mapprojection.previewmap.PreviewMapCallback;
import com.mapgame.streetsgraph.model.CrossroadNode;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Way;

public class RaceActivity extends MapActivity 
		implements ArrowsDisplayableActivity {

	View pauseScreen;
	View turnArrowsView;
	TextView nextStreetView;
	
	Game game;
	
	@Override
	protected void mapLayoutInitialize() {
		setContentView(R.layout.activity_race);
		setMap((MapView) findViewById(R.id.mapview));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pauseScreen = findViewById(R.id.pauseScreen);
		resumeSetUp();
		turnArrowsView = (RelativeLayout) findViewById(R.id.buttonsPanel);
		nextStreetView = (TextView) findViewById(R.id.street);
		
		disableMapTouch(getMap());
		
		game = new Game(this);
	}

	private void disableMapTouch(MapView map) {
		map.setBuiltInZoomControls(false);
		map.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
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
	
	PreviewMapCallback callback;
	
	public void showRaceIntro(CrossroadNode start, CrossroadNode end, PreviewMapCallback callback) {
		this.callback = callback;
		Intent intent = new Intent(getApplicationContext(), RaceIntroActivity.class);
		Bundle b = new Bundle();
		b.putSerializable("start", start);
		b.putSerializable("end", end);
		intent.putExtras(b);
		startActivityForResult(intent, 1);
	}
	
	public void showRaceFinish(Point start, Point end, LinkedList<Way> userWay,
			LinkedList<Point> bestWay, PreviewMapCallback callback) {
		this.callback = callback;
		Intent intent = new Intent(getApplicationContext(), RaceFinishActivity.class);
		Bundle b = new Bundle();
		b.putParcelable("start", start);
		b.putParcelable("end", end);
		b.putSerializable("userRoute", userWay);
		b.putSerializable("bestRoute", bestWay);
		intent.putExtras(b);
		startActivityForResult(intent, 2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 || requestCode == 2) {
			callback.onPreviewFinished();
		}
	}

	public void addStartFlagToMap(Point start) {
		drawFlagOnMap(R.drawable.start_flag, new OverlayItem("Start", "", start));
	}

	public void addEndFlagToMap(Point end) {
		drawFlagOnMap(R.drawable.end_flag, new OverlayItem("Destination", "", end));
	}
	
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
