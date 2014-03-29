package com.mapgame.overlaycomponents;

import com.mapgame.R;

import android.app.Activity;
import android.widget.TextView;

public class LengthCounter {
	double length;
	double cost;
	
	TextView lengthCounter;
	TextView costCounter;
	
	Activity activity;
	
	public LengthCounter(Activity activity) {
		this.length = 0;
		this.cost = 0;
		this.activity = activity;
		lengthCounter = (TextView) activity.findViewById(R.id.length);
		costCounter = (TextView) activity.findViewById(R.id.cost);
	}

	public double getLength() {
		return (double)Math.round(length)/1000;
	}
	
	public void addLength(double lengthToAdd) {
		this.length += lengthToAdd;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				lengthCounter.setText(Double.toString(getLength())+" km");
			}
		});
	}
	
	public double getCost() {
		return (double)Math.round(cost/6)/10;
	}
	
	public void addCost(double costToAdd) {
		this.cost += costToAdd;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				costCounter.setText(Double.toString(getCost())+" min");
			}
		});
	}
}
