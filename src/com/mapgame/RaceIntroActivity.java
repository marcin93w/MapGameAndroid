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
		final CrossroadNode end = (CrossroadNode) b.getSerializable("end");
		
		final TextView street = (TextView) findViewById(R.id.preview_street_name);
		final TextView label = (TextView) findViewById(R.id.TextView02);
		
		label.setText(R.string.source);
		street.setText(start.getWay().getRoad().getName());
		
		PreviewMap previewController = new PreviewMap(this, new Point(50.065404,19.949255));
		previewController.showIntroPreview(start.getWay().getFirstPoint(), 
				end.getCrossroadPoint(), 
			new PreviewMapCallback() {
				@Override
				public void onPreviewFinished() {
					runOnUiThread(new Runnable() {	
						@Override
						public void run() {
							label.setText(R.string.target);
							street.setText(end.getWay().getRoad().getName());
						}
					});
				}
			},
			new PreviewMapCallback() {
				@Override
				public void onPreviewFinished() {
					invokeEndPreview();	
				}
		});
	}
}
