package com.mapgame;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.mapgame.mapprojection.previewmap.PreviewMap;
import com.mapgame.mapprojection.previewmap.PreviewMapCallback;
import com.mapgame.streetsgraph.model.CrossroadNode;
import com.mapgame.streetsgraph.model.Point;



public class RaceIntroActivity extends RacePreviewActivity {

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_raceintro);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getMap().setBuiltInZoomControls(false);
		getMap().setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		Bundle b = getIntent().getExtras();
		CrossroadNode start = (CrossroadNode) b.getSerializable("start");
		CrossroadNode end = (CrossroadNode) b.getSerializable("end");
		
		TextView startStreet = (TextView) findViewById(R.id.preview_start);
		TextView endStreet = (TextView) findViewById(R.id.preview_destination);
		
		startStreet.setText(start.getWay().getRoad().getName());
		endStreet.setText(end.getWay().getRoad().getName());
		
		PreviewMap previewController = new PreviewMap(this, new Point(50.065404,19.949255));
		previewController.showIntroPreview(start.getWay().getFirstPoint(), 
				end.getCrossroadPoint(), new PreviewMapCallback() {
			@Override
			public void onPreviewFinished() {
				invokeEndPreview();	
			}
		});
	}
}
