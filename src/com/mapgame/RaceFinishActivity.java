package com.mapgame;

import java.util.ArrayList;
import java.util.LinkedList;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mapgame.mapprojection.previewmap.PreviewMap;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Way;

public class RaceFinishActivity extends RacePreviewActivity {

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
		LinkedList<Way> userRoute = 
				new LinkedList<Way>((ArrayList<Way>) b.getSerializable("userRoute"));
		LinkedList<Point> bestRoute = 
				new LinkedList<Point>((ArrayList<Point>) b.getSerializable("bestRoute"));
		
		PreviewMap previewController = new PreviewMap(this, new Point(50.065404,19.949255));
		previewController.showOutroPreview(start, end, userRoute, bestRoute);
		
		Button nextGame = (Button) findViewById(R.id.playagain);
		nextGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				endPreview();
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
