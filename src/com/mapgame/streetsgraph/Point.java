package com.mapgame.streetsgraph;

import org.osmdroid.util.GeoPoint;

public class Point extends GeoPoint {
	private static final long serialVersionUID = 1L;

	public Point(double aLatitude, double aLongitude) {
		super(aLatitude, aLongitude);
	}

	public double lonLatDistance(Point other) {
		return Math.sqrt((other.getLongitude() - this.getLongitude())
				* (other.getLongitude() - this.getLongitude())
				+ (other.getLatitude() - this.getLatitude())
				* (other.getLatitude() - this.getLatitude()));
	}
	
	public boolean isBefore(Point other, boolean lonBackward, boolean latBackward) {
		if((this.getLatitude() < other.getLatitude()) && !latBackward) {
			if((this.getLongitude() < other.getLongitude()) && !lonBackward) {
				return true;
			}
			if((this.getLongitude() > other.getLongitude()) && lonBackward) {
				return true;
			}
		}
		if((this.getLatitude() > other.getLatitude()) && latBackward) {
			if((this.getLongitude() < other.getLongitude()) && !lonBackward) {
				return true;
			}
			if((this.getLongitude() > other.getLongitude()) && lonBackward) {
				return true;
			}
		}
		
		return false;
	}
}
