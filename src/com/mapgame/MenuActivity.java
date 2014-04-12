package com.mapgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MenuActivity extends Activity {
	Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_menu);
		context = getApplicationContext();
		
		Button start = (Button) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
			}
		});
	}
}
