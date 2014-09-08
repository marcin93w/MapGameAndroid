package com.mapgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MenuActivity extends Activity {
	private Context context;
	private Vibrator vibe;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_menu);
		context = getApplicationContext();
		vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		
		ImageButton start = (ImageButton) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				vibe.vibrate(100);
				Intent intent = new Intent(context, RaceActivity.class);
				startActivity(intent);
			}
		});
		
		ImageButton exit = (ImageButton) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibe.vibrate(100);
				finish();				
			}
		});
	}
}
