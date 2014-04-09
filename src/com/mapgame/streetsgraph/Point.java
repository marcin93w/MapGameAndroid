package com.mapgame.streetsgraph;

import org.osmdroid.util.GeoPoint;

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
	
	/*/
	public Point move(double lonLatDistance, double lonDirection, double latDirection) {
		DirectionVector vector = new DirectionVector(lonDirection, latDirection);
		vector.scaleToMagnitude(lonLatDistance);
		return new Point(this.getLatitude() + vector.getB(),
				this.getLongitude() + vector.getA());
	}
	/*/
	
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
	
}
