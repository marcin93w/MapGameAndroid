package com.mapgame.streetsgraph;

import com.mapgame.engine.DirectionVector;

public class Road {
	Way way;
	boolean backward;
	
	public Road(Way way, boolean backward) {
		this.way = way;
		this.backward = backward;
	}
	
	public int getEndCrossroadNode() {
		if(backward)
			return way.getStartNodeId();
		else
			return way.getEndNodeId();
	}

	public Way getWay() {
		return way;
	}

	public boolean isBackward() {
		return backward;
	}
	
	public Road reverse() {
		backward = !backward;
		return this;
	}
	
	public enum Position { START, END };
	
	public DirectionVector createDirectionVector(Position position) {
		if(position == Position.START)
			return createDirectionVector(0);
		else
			return createDirectionVector(way.getGeometry().size()-1);
	}
	
	public DirectionVector createDirectionVector(int pointId) {
		Point a,b;
		if(pointId < way.getGeometry().size()-1) {
			a = way.getGeometry().get(pointId); 
			b = way.getGeometry().get(pointId+1);
		} else {
			a = way.getGeometry().get(pointId-1);
			b = way.getGeometry().get(pointId);
		}
		
		if(backward) {
			return (new DirectionVector(b,a));
		} else {
			return (new DirectionVector(a,b));
		}
	}
	
}
