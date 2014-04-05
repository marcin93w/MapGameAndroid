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
    	return road.getEndCrossroadNode();
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
				if(child.getNodeId() != parent.getNodeId()) {
					list.add(child);
				}
			}
			return list;
		}
	}
	
	public boolean isReturnToParent(CrossroadNode child) {
		if(child.getNodeId() == road.getStartCrossroadNode())
			return true;
		return false;
	}

	public void addChildren(List<Road> roadsToChildren) {
		children = new ArrayList<CrossroadNode>();
		for(Road r : roadsToChildren) {
			CrossroadNode childNode = new CrossroadNode(r);
			childNode.parent = this;
			children.add(childNode);
		}
		
		//if its a one way, dead road - enable turn around
		if(children.size() == 0) {
			CrossroadNode backNode = new CrossroadNode(
					new Road(road.getWay(), !road.isBackward()));
			backNode.parent = this;
			children.add(backNode);
		}
	}
	
	
	public int getIndexOfMostForwardOffspring(List<CrossroadNode> offspring) {
		double angle = Double.MAX_VALUE;
		int idx = 0;

		for (int i = 0; i < offspring.size(); i++) {
			DirectionVector vectorToChild = offspring.get(i).getRoad()
					.getDirectionVector(Road.Position.START);
			DirectionVector vectorToThis = road
					.getDirectionVector(Road.Position.END);
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
	
	public int getLevel() {
		int level = 0;
		CrossroadNode p = parent;
		while(p != null) {
			p = p.parent;
			level++;
		}
		
		return level;
	}

	public CrossroadNode getParent() {
		return parent;
	}
}
