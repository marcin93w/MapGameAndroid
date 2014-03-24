package com.mapgame.streetsgraph;


/*
 * Class represents position (point) that car can be on
 */
public class CarPosition {
	Way way;
	int pointId;
	boolean backward;
	
	public CarPosition(Way way, boolean backward) {
		setWay(way, backward);
	}
	
	public void setWay(Way way, boolean backward) {
		this.way = way;
		this.backward = backward;
		this.pointId = backward ? way.getGeometry().size() - 1 : 0;

	}

	public Point getPoint() {
		return way.getGeometry().get(pointId);
	}
	
	public Point getNextPoint() {
		pointId += backward ? -1 : 1;
		return getPoint();
	}
	
	public Point getPrevPoint() {
		pointId += backward ? 1 : -1;
		return getPoint();
	}
	
	public boolean isOnCrossroad() {
		if(backward && pointId == 0)
			return true;
		if(!backward && pointId == way.getGeometry().size()-1)
			return true;
		return false;
	}
	
	/*
	 * Returns node id of next crossroad
	 * (if car is on crossroad returns node id of this crossroad
	 * else returns node id of crossroad that car is coming to)
	 */
	public int getCrossroadNode() {
		if(backward)
			return way.getStartNode();
		else
			return way.getEndNode();
	}

	public Way getWay() {
		return way;
	}

	public boolean isBackward() {
		return backward;
	}
	
	public DirectionVector getDirectionVector() {
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
