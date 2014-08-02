package com.mapgame.streetsgraph.model;

import org.osmdroid.util.GeometryMath;

import com.mapgame.arrowsturn.TurnArrows;


public class DirectionVector {
	double x;
	double y;

	double magnitude;
	
	public DirectionVector(Point p1, Point p2, double magnitude) {
		this(p1.bearingTo(p2), magnitude);
	}
	
	public DirectionVector(Point p1, Point p2) {
		this(p1, p2, TurnArrows.distanceFromCenter);
	}
	
	public DirectionVector(double bearing, double magnitude) {
		this.magnitude = magnitude;
		bearing *= GeometryMath.DEG2RAD;
		this.x = magnitude * Math.sin(bearing);
		this.y = magnitude * Math.cos(bearing);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double a) {
		this.x = a;
	}

	public void setY(double b) {
		this.y = b;
	}

	public void scaleToMagnitude(double size) {
		if(magnitude != size) {
			double scale = Math.sqrt((size * size) / (x * x + y * y));
			x = x * scale;
			y = y * scale;
			magnitude = size;
		}
	}
}
