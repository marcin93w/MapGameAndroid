package com.mapgame;

import java.util.LinkedList;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.PathOverlay;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.mapgame.mapprojection.previewmap.PreviewMap;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Route;

public class RaceFinishActivity extends RacePreviewActivity {

	private Animation animUp;
	private Button nextGame;
	
	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_racefinish);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getMap().setBuiltInZoomControls(true);
		
		Bundle b = getIntent().getExtras();
		Point start = new Point( (GeoPoint) b.getParcelable("start"));
		Point end = new Point( (GeoPoint) b.getParcelable("end"));
		Route userRoute = (Route) b.getSerializable("userRoute");
		Route bestRoute = (Route) b.getSerializable("bestRoute");
		
		PreviewMap previewController = new PreviewMap(this, new Point(50.065404,19.949255));
		previewController.showOutroPreview(start, end, userRoute, bestRoute);
		
		nextGame = (Button) findViewById(R.id.playagain);
		nextGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				endPreview();
			}
		});
		
		displayStats(userRoute, bestRoute);
		setupStatsSlideAnimation();
	}

	private void displayStats(Route userRoute, Route bestRoute) {
		TextView result = (TextView) findViewById(R.id.stats_result);
		TextView userCost = (TextView) findViewById(R.id.stats_usercost);
		TextView userLength = (TextView) findViewById(R.id.stats_userlength);
		TextView bestCost = (TextView) findViewById(R.id.stats_bestcost);
		TextView bestLength = (TextView) findViewById(R.id.stats_bestlength);
		
		userCost.setText(doubleToCostString(userRoute.getCost()));
		userLength.setText(doubleToLengthString(userRoute.getLength()));
		bestCost.setText(doubleToCostString(bestRoute.getCost()));
		bestLength.setText(doubleToLengthString(bestRoute.getLength()));
		
		float resultProcentage = (float)((userRoute.getCost() - bestRoute.getCost()) 
				/ bestRoute.getCost() * 100);
		resultProcentage = Math.round(resultProcentage*10)/10f;
		result.setText("+ " + Float.toString(resultProcentage) + "%");
	}
	
	private String doubleToCostString(double costInSeconds) {
		int minutes = (int) costInSeconds/60;
		double seconds = costInSeconds - minutes * 60;
		return Integer.toString(minutes) + " min " + Long.toString(Math.round(seconds)) + " sec";
	}
	
	private String doubleToLengthString(double length) {
		return Double.toString(Math.round(length*10)/10.0) + " m";
	}

	private void setupStatsSlideAnimation() {
		animUp = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_up);
		
		final View statsLayout = findViewById(R.id.statslayout);
		statsLayout.setOnTouchListener(new OnTouchListener() {
			private float startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					if (event.getY() < startY) {
						animUp.setAnimationListener(new AnimationListener() {		
							@Override
							public void onAnimationStart(Animation animation) {
								statsLayout.setOnTouchListener(null);
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) { }
							
							@Override
							public void onAnimationEnd(Animation animation) {
								statsLayout.setVisibility(View.GONE);
								nextGame.setVisibility(View.VISIBLE);
							}
						});
						statsLayout.startAnimation(animUp);
					}
				}
				return true;
			}
		});
	}

	public void addPathToPreviewMap(final LinkedList<Point> path, final int color) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				PathOverlay po = new PathOverlay(color, getApplicationContext());
				po.addPoints(new LinkedList<IGeoPoint>(path));
				getMap().getOverlays().add(po);
			}
		});
	}
	
}
