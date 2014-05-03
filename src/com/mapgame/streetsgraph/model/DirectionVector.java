package com.mapgame.streetsgraph.model;

import org.osmdroid.util.GeometryMath;

import com.mapgame.arrowsturn.TurnArrows;


public class DirectionVector {
	double a;
	double b;

	double magnitude;
	
	public DirectionVector(Point p1, Point p2, double magnitude) {
		this.magnitude = magnitude;
		double angle = GeometryMath.DEG2RAD * p1.bearingTo(p2);
		this.a = magnitude * Math.sin(angle);
		this.b = magnitude * Math.cos(angle);
	}
	
	public DirectionVector(Point p1, Point p2) {
		this(p1, p2, TurnArrows.distanceFromCenter);
	}

	public DirectionVector(double a, double b) {
		this.a = a;
		this.b = b;
	}

	//TODO usunac z tad te funkcje i brac katy bezposrednio z Pointsow poprzez bearing
	public double getAbsAngle(DirectionVector other) {
		double cos = (this.a * other.a + this.b * other.b)
				/ (Math.sqrt(this.a * this.a + this.b * this.b) * 
						Math.sqrt(other.a * other.a + other.b * other.b));
		if(cos > 1) cos = 1;
		else if (cos < -1) cos = -1;
		
		return Math.acos(cos);
	}

	public double getAngleInDegrees(DirectionVector other) {
		double angle = getAbsAngle(other);
		if (b > 0)
			angle = -angle;
		return radToDegrees(angle);
	}
	
	public double getAbsAngleInDegrees(DirectionVector other) {
		return radToDegrees(getAbsAngle(other));
	}

	private double radToDegrees(double rad) {
		return (rad / Math.PI) * 180;
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public void setA(double a) {
		this.a = a;
	}

	public void setB(double b) {
		this.b = b;
	}

	public void scaleToMagnitude(double size) {
		if(magnitude != size) {
			double scale = Math.sqrt((size * size) / (a * a + b * b));
			a = a * scale;
			b = b * scale;
			magnitude = size;
		}
	}

	public DirectionVector rotate(int degrees) {
		double rad = ((double) degrees / 180) * Math.PI;
		a = a * Math.cos(rad) + b * Math.sin(rad);
		b = a * (-Math.sin(rad)) + b * Math.cos(rad);
		return this;
	}
}
