package com.mapgame.streetsgraph.model;

import android.annotation.SuppressLint;
import org.osmdroid.util.GeoPoint;

@SuppressLint("ParcelCreator") 
public class Point extends GeoPoint {
	private static final long serialVersionUID = 1L;
	
	public Point(double aLatitude, double aLongitude) {
		super(aLatitude, aLongitude);
	}
	
	public Point(int aLatitudeE6, int aLongitudeE6) {
		super(aLatitudeE6, aLongitudeE6);
	}
	
	public Point(GeoPoint geoPoint) {
		super(geoPoint);
	}
	
	public double getLonLatDistance(Point other) {
		double dis2 = Math.pow(other.getLongitude() - this.getLongitude(), 2) +
					Math.pow(other.getLatitude() - this.getLatitude(), 2);
		return Math.sqrt(dis2)*1000000;
	}
	
	public boolean isBefore(Point other, boolean lonBackward, boolean latBackward) {
		//check longer direction
		if(Math.abs(this.getLongitudeE6() - other.getLongitudeE6()) > 
				Math.abs(this.getLatitudeE6() - other.getLatitudeE6())) {
			if(this.getLongitudeE6() < other.getLongitudeE6() && !lonBackward)
				return true;
			if(this.getLongitudeE6() > other.getLongitudeE6() && lonBackward)
				return true;
		} else {
			if(this.getLatitudeE6() < other.getLatitudeE6() && !latBackward)
				return true;
			if(this.getLatitudeE6() > other.getLatitudeE6() && latBackward)
				return true;
		}
		
		return false;
	}
	
	@Override
	public Point clone() {
		return new Point(getLatitudeE6(), getLongitudeE6());
	}
	
}
