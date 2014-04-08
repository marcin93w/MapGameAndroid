package com.mapgame.streetsgraph;

import org.osmdroid.util.GeoPoint;

import com.mapgame.engine.DirectionVector;

public class Point extends GeoPoint {
	private static final long serialVersionUID = 1L;

	public Point(double aLatitude, double aLongitude) {
		super(aLatitude, aLongitude);
	}
	
	public Point(int aLatitudeE6, int aLongitudeE6) {
		super(aLatitudeE6, aLongitudeE6);
	}

	public double getLonLatDistance(Point other) {
		return Math.sqrt((other.getLongitude() - this.getLongitude())
				* (other.getLongitude() - this.getLongitude())
				+ (other.getLatitude() - this.getLatitude())
				* (other.getLatitude() - this.getLatitude()));
	}
	
	public double getMercatorDistance(Point other) {
		return Math.sqrt((other.getLongitudeE6() - this.getLongitudeE6())
				* (other.getLongitudeE6() - this.getLongitudeE6())
				+ (other.getLatitudeE6() - this.getLatitudeE6())
				* (other.getLatitudeE6() - this.getLatitudeE6()));
	}
	
	public Point move(double lonLatDistance, double lonDirection, double latDirection) {
		DirectionVector vector = new DirectionVector(lonDirection, latDirection);
		vector.scaleToMagnitude(lonLatDistance);
		return new Point(this.getLatitude() + vector.getB(),
				this.getLongitude() + vector.getA());
	}
	
	public boolean isBefore(Point other, boolean lonBackward, boolean latBackward) {
		//check longer direction
		if(Math.abs(this.getLongitude() - other.getLongitude()) > 
				Math.abs(this.getLatitude() - other.getLatitude())) {
			if(this.getLongitude() < other.getLongitude() && !lonBackward)
				return true;
			if(this.getLongitude() > other.getLongitude() && lonBackward)
				return true;
		} else {
			if(this.getLatitude() < other.getLatitude() && !latBackward)
				return true;
			if(this.getLatitude() > other.getLatitude() && latBackward)
				return true;
		}
		
		return false;
	}
	
}
