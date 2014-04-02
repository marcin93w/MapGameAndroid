package com.mapgame.streetsgraph;

import java.util.ArrayList;
import java.util.List;

import com.mapgame.engine.DirectionVector;

public class CrossroadNode {
	private Road road; //road from parent
    private CrossroadNode parent;
    private List<CrossroadNode> children;
    private CrossroadNode selectedChild;
	
    public CrossroadNode(Road data) {
		this.road = data;
	}
    
    public int getNodeId() {
    	return road.way.getEndNodeId();
    }
    
    public Road getRoad() {
    	return road;
    }

	public CrossroadNode getSelectedChild() {
		return selectedChild;
	}

	public void select() {
		if(parent != null) {
			parent.selectedChild = this;
			parent.select();
		}
	}

	public List<CrossroadNode> getChildren() {
		if(parent == null || children == null) {
			return children;
		} else {
			//return children without parent node
			List<CrossroadNode> list = new ArrayList<CrossroadNode>();
			for(CrossroadNode child : children) {
				if(child.getNodeId() != road.getWay().getStartNodeId()) {
					list.add(child);
				}
			}
			return list;
		}
	}

	public void addChildren(List<Road> roadsToChildren) {
		children = new ArrayList<CrossroadNode>();
		for(Road r : roadsToChildren) {
			CrossroadNode childNode = new CrossroadNode(r);
			childNode.parent = this;
			children.add(childNode);
		}
		selectedChild = getDefaultChild();
	}
	
	private CrossroadNode getDefaultChild() {
		return children.get(getIndexOfDefaultChild());
	}
	
	private int getIndexOfDefaultChild() {
		double angle = Double.MAX_VALUE;
		int idx = 0;

		for (int i = 0; i < children.size(); i++) {
			DirectionVector vectorToChild = children.get(i).getRoad()
					.createDirectionVector(Road.Position.START);
			DirectionVector vectorToThis = road
					.createDirectionVector(Road.Position.END);
			double calculatedAngle = vectorToChild.getAbsAngle(vectorToThis);
			if (calculatedAngle < angle) {
				angle = calculatedAngle;
				idx = i;
			}
		}

		return idx;
	}
	
	public CrossroadNode separate() {
		parent = null;
		return this;
	}
}
