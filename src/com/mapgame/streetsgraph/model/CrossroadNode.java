package com.mapgame.streetsgraph.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mapgame.streetsgraph.model.Way.Position;

/*
 * Node of a tree
 */
public class CrossroadNode implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Way way; //way from parent
    private CrossroadNode parent;
    private List<CrossroadNode> children;
    private CrossroadNode selectedChild;
	
    public CrossroadNode(Way data) {
		this.way = data;
	}
    
    public int getNodeId() {
    	return way.getEndCrossroadNodeId();
    }
    
    public Way getWay() {
    	return way;
    }

	public CrossroadNode getSelectedChild() {
		return selectedChild;
	}

	public void setSelectedChild(CrossroadNode selectedChild) {
		this.selectedChild = selectedChild;
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
			//return children excluding parent node
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
		if(child.getNodeId() == way.getStartCrossroadNodeId())
			return true;
		return false;
	}

	public void setChildren(List<Way> roadsToChildren) {
		children = new ArrayList<CrossroadNode>();
		for(Way r : roadsToChildren) {
			CrossroadNode childNode = new CrossroadNode(r);
			childNode.parent = this;
			children.add(childNode);
		}
		
		//if its a one way, dead road - add turn around node
		//FIXME if this turn around node has only oneway and backward children
		//race loops for infinity
		if(children.size() == 0) {
			CrossroadNode backNode = new CrossroadNode(
					new Way(way.getRoad(), !way.isBackward()));
			backNode.parent = this;
			children.add(backNode);
		}
	}
	
	public CrossroadNode getMostForwardNode(List<CrossroadNode> nodes) {
		return getMostForwardNode(nodes, Double.MAX_VALUE);
	}
	
	public CrossroadNode getMostForwardNode(List<CrossroadNode> nodes, double maxAngle) {
		double currentAzimuth = way.getAzimuth(Position.END);
		double angle = Double.MAX_VALUE;
		int idx = -1;

		for (int i = 0; i < nodes.size(); i++) {
			double calculatedAngle = Math.abs(
					nodes.get(i).getWay().getAzimuth(Position.START) - currentAzimuth);
			if(calculatedAngle > 180)
				calculatedAngle = 360 - calculatedAngle;
			
			if (calculatedAngle <= angle && calculatedAngle <= maxAngle) {
				angle = calculatedAngle;
				idx = i;
			}
		}

		return idx > -1 ? nodes.get(idx) : null;
	}
	
	public CrossroadNode getMostForwardChild() {
		return getMostForwardNode(children);
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
