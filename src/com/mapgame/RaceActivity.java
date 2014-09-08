package com.mapgame;

import java.util.ArrayList;

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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapgame.arrowsturn.ArrowsDisplayableActivity;
import com.mapgame.engine.Game;
import com.mapgame.mapprojection.gamemap.SpeedChangeListener;
import com.mapgame.mapprojection.previewmap.PreviewMapCallback;
import com.mapgame.overlaycomponents.RaceCountdownAnimation;
import com.mapgame.overlaycomponents.RaceCountdownAnimation.Callback;
import com.mapgame.streetsgraph.model.CrossroadNode;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Route;

public class RaceActivity extends MapActivity 
		implements ArrowsDisplayableActivity, RaceCountdownAnimation.CountdownActivity {

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
	
	public void setDestinationText(String text) {
		TextView dest = (TextView)findViewById(R.id.destiantion);
		dest.setText(text);
	}
	
	public void setOnGearChangedListener(final SpeedChangeListener scl) {
		final ArrayList<ImageButton> gears = new ArrayList<ImageButton>();
		gears.add((ImageButton) findViewById(R.id.gear_1));
		gears.add((ImageButton) findViewById(R.id.gear_2));
		gears.add((ImageButton) findViewById(R.id.gear_3));
		gears.add((ImageButton) findViewById(R.id.gear_4));
		gears.add((ImageButton) findViewById(R.id.gear_r));
		OnClickListener l = new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				switch(v.getId()) {
				case R.id.gear_4:
					scl.setSpeed(4);
					break;
				case R.id.gear_3:
					scl.setSpeed(3);
					break;
				case R.id.gear_2:
					scl.setSpeed(2);
					break;
				case R.id.gear_1:
					scl.setSpeed(1);
					break;
				case R.id.gear_r:
					scl.setSpeed(-1);
				}
				for(ImageButton gear : gears) {
					gear.setBackgroundDrawable(null);
				}
				((ImageButton)v).setBackgroundDrawable(
						getResources().getDrawable(R.drawable.selected_gear));
			}
		};
		for(ImageButton gear : gears) {
			gear.setOnClickListener(l);
		}
		scl.setSpeed(3);
	}

	public void initializeCarSurfaceView(SurfaceHolder.Callback callback) {
		SurfaceView carSurface = (SurfaceView) findViewById(R.id.surfaceView1);
		carSurface.setZOrderOnTop(true);
		carSurface.setVisibility(View.VISIBLE);
		SurfaceHolder carSufraceHolder = carSurface.getHolder();
		carSufraceHolder.setFormat(PixelFormat.TRANSPARENT);
		carSufraceHolder.addCallback(callback);
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
		
		Button exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exit();
			}
		});
	}
	
	private void exit() {
		super.onBackPressed();
	}
	
	@Override
	protected void onPause() {
		if(game.pause())
			pauseScreen.setVisibility(View.VISIBLE);
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		if(game.pause())
			pauseScreen.setVisibility(View.VISIBLE);
		else
			exit();
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
	
	public void showRaceFinish(Point start, Point end, Route route,
			Route bestRoute, PreviewMapCallback callback) {
		this.callback = callback;
		Intent intent = new Intent(getApplicationContext(), RaceFinishActivity.class);
		Bundle b = new Bundle();
		b.putParcelable("start", start);
		b.putParcelable("end", end);
		b.putSerializable("userRoute", route);
		b.putSerializable("bestRoute", bestRoute);
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
	public void setStreetNameView(final String text) {
		if(!nextStreetView.getText().equals(text)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					nextStreetView.setText(text);
				}
			});
		}
	}

	///Race Countdown
	public void startCountdown(final RaceCountdownAnimation.Callback raceCountdownCallback) {
		final View contdownView = findViewById(R.id.countdownScreen);
		contdownView.setVisibility(View.VISIBLE);
		(new RaceCountdownAnimation(new Callback() {
			@Override
			public void raceCountdownFinished() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						contdownView.setVisibility(View.GONE);	
					}
				});
				raceCountdownCallback.raceCountdownFinished();
			}
		}, this)).start();
	}
	
	public void updateCountdownCounter(final String text) {
		final TextView countdown = (TextView) findViewById(R.id.countdownText);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				countdown.setText(text);
			}
		});
	}
	
}
