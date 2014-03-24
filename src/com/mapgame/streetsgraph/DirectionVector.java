package com.mapgame.streetsgraph;


public class DirectionVector {
	double a;
	double b;
	
	public DirectionVector(Point p1, Point p2) {
		this.a = p2.getLongitude() - p1.getLongitude();
	    this.b = p2.getLatitude() - p1.getLatitude();
	}
	
	public double getAngle (DirectionVector other) {
	    return Math.acos(
	            (this.a*other.a + this.b*other.b) / 
	            (Math.sqrt(this.a*this.a+this.b*this.b)*Math.sqrt(other.a*other.a+other.b*other.b))
	        );
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
	
	public void scaleToMagnitude(int size) {
		double scale = Math.sqrt(((double)size*size)/(a*a+b*b));
		a = a*scale;
		b = b*scale;
	}
	
	public DirectionVector rotate (int degrees) {
	    double rad = ((double)degrees/180)*Math.PI;
	    a = a * Math.cos(rad) + b * Math.sin(rad);
	    b = a * (-Math.sin(rad)) + b * Math.cos(rad);
	    return this;
	}
}
