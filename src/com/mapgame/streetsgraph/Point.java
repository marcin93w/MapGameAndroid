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
	
	//FIXME na równych odcinkach isBefore zwraca false za wczesnie
	//pewnie trzeba przebudować na odwrotne
	//i chyba funkcje isFirstLower/Greater są nie potrzebne - spróbować usunąć
	public boolean isBefore(Point other, boolean lonBackward, boolean latBackward) {
		if(isFirstLower(this.getLatitude(), other.getLatitude()) && !latBackward) {
			if(isFirstLower(this.getLongitude(), other.getLongitude()) && !lonBackward) {
				return true;
			}
			if(isFirstGreater(this.getLongitude(), other.getLongitude()) && lonBackward) {
				return true;
			}
		}
		if(isFirstGreater(this.getLatitude(), other.getLatitude()) && latBackward) {
			if(isFirstLower(this.getLongitude(), other.getLongitude()) && !lonBackward) {
				return true;
			}
			if(isFirstGreater(this.getLongitude(), other.getLongitude()) && lonBackward) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isFirstLower(double first, double other, double tolerance) {
		if((first - other) < tolerance)
			return true;
		return false;
	}
	
	private boolean isFirstGreater(double first, double other, double tolerance) {
		if((first - other) > -tolerance)
			return true;
		return false;
	}
	
	private boolean isFirstLower(double first, double other) {
		return isFirstLower(first, other, Double.MIN_VALUE*10);
	}
	
	private boolean isFirstGreater(double first, double other) {
		return isFirstGreater(first, other, Double.MIN_VALUE*10);
	}
	
	public boolean equals(Point other, double lonTolerance, double latTolerance) {
		if(Math.abs(this.getLatitude() - other.getLatitude()) < latTolerance) {
			if(Math.abs(this.getLongitude() - other.getLongitude()) < lonTolerance) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Point) {
			return equals((Point)other, Double.MIN_VALUE, Double.MIN_VALUE);
		}
		return false;
	}
}
