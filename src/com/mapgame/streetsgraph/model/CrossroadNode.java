package com.mapgame.streetsgraph.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Node of a tree
 */
public class CrossroadNode {
	private Way way; //way from parent
    private CrossroadNode parent;
    private List<CrossroadNode> children;
    private CrossroadNode selectedChild;
	
    public CrossroadNode(Way data) {
		this.way = data;
	}
    
    public int getNodeId() {
    	return way.getEndCrossroadNode();
    }
    
    public Way getWay() {
    	return way;
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
		if(child.getNodeId() == way.getStartCrossroadNode())
			return true;
		return false;
	}

	public void addChildren(List<Way> roadsToChildren) {
		children = new ArrayList<CrossroadNode>();
		for(Way r : roadsToChildren) {
			CrossroadNode childNode = new CrossroadNode(r);
			childNode.parent = this;
			children.add(childNode);
		}
		
		//if its a one way, dead road - enable turn around
		if(children.size() == 0) {
			CrossroadNode backNode = new CrossroadNode(
					new Way(way.getRoad(), !way.isBackward()));
			backNode.parent = this;
			children.add(backNode);
		}
		
		selectedChild = children.get(
				getIndexOfMostForwardNode(children));
	}
	
	public int getIndexOfMostForwardNode(List<CrossroadNode> nodes) {
		double angle = Double.MAX_VALUE;
		int idx = 0;

		for (int i = 0; i < nodes.size(); i++) {
			DirectionVector vectorToChild = nodes.get(i).getWay()
					.getDirectionVector(Way.Position.START);
			DirectionVector vectorToThis = way
					.getDirectionVector(Way.Position.END);
			double calculatedAngle = vectorToChild.getAbsAngle(vectorToThis);
			
			//exception
			if(this.way.road.getName().equals(nodes.get(i).way.road.getName()) &&
					calculatedAngle < Math.PI/4)
				return i;
			
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
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof CrossroadNode) {
			if(this.getNodeId() == ((CrossroadNode)other).getNodeId())
				return true;
		}
		return false;
	}
	
	public boolean haveInAncestry(CrossroadNode node) {
		CrossroadNode thisNode = this;
		while(thisNode != null && !thisNode.equals(node)) {
			thisNode = thisNode.getParent();
		}
		
		if(thisNode == null)
			return false;
		else
			return true;
	}
	
	public Point getCrossroadPoint() {
		return way.getLastPoint();
	}
}
