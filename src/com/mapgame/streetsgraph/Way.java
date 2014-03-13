package com.mapgame.streetsgraph;

import java.util.ArrayList;

public class Way {
	ArrayList<Point> points;
	boolean oneway;
	
	//neighbors
	ArrayList<Way> prevForward;
	ArrayList<Way> prevBackward;
	ArrayList<Way> nextForward;
	ArrayList<Way> nextBackward;
	
	public Way(ArrayList<Point> points, boolean oneway) {
		super();
		this.points = points;
		this.oneway = oneway;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public boolean isOneway() {
		return oneway;
	}

	public ArrayList<Way> getPrevForward() {
		return prevForward;
	}

	public void setPrevForward(ArrayList<Way> prevForward) {
		this.prevForward = prevForward;
	}

	public ArrayList<Way> getPrevBackward() {
		return prevBackward;
	}

	public void setPrevBackward(ArrayList<Way> prevBackward) {
		this.prevBackward = prevBackward;
	}

	public ArrayList<Way> getNextForward() {
		return nextForward;
	}

	public void setNextForward(ArrayList<Way> nextForward) {
		this.nextForward = nextForward;
	}

	public ArrayList<Way> getNextBackward() {
		return nextBackward;
	}

	public void setNextBackward(ArrayList<Way> nextBackward) {
		this.nextBackward = nextBackward;
	}
	
}
