package com.mapgame.turnsensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class TurnSensor implements SensorEventListener {

	private Turnable receiver;
	
	public TurnSensor(Turnable receiver) {
		this.receiver = receiver;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {		
		receiver.turn(calculateAngle(event.values[0], event.values[1], event.values[2])*1);
	}
	
	private int calculateAngle(float aX, float aY, float aZ) {
		// These numbers can creep outside of the interval -1 to 1 if the phone is even moving very slightly, 
		//so we use the following lines to keep the values between -1 and 1.
		if (aX < -1) aX = -1;
		if (aX > 1) aX = 1;
		if (aY < -1) aY = -1;
		if (aY > 1) aY = 1;
		if (aZ < -1) aZ = -1;
		if (aZ > 1) aZ = 1;
	
		/*
		We calculate the angle by using the vectory identity u.v = |u| |v| cos(angle), 
		where u is the vector (aX,aY,aZ) and v is the vector (0,aY,0) which points vertically. 
		We have to subract 90 because arccos essentially returns values between 0 and 180, 
		and we would like to interpret these between -90 and 90.
		*/
		double mag = Math.sqrt(aX*aX+aY*aY+aZ*aZ);
		int angle = (int)Math.round((180/Math.PI)*Math.acos(aY/mag)) - 90;
	
		// We do not allow the angle to get outside of the range -90 to 90.
		if (angle < -90) angle = -90;
		if (angle > 90) angle = 90;
		
		return -angle;
	}

}
