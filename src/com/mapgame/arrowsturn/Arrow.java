package com.mapgame.arrowsturn;

import com.mapgame.streetsgraph.CrossroadNode;

public class Arrow {
	CrossroadNode node;
	boolean main;
	boolean active; //arrows replaced by child arrows are marked as non active
	
	public Arrow(CrossroadNode node, boolean main) {
		this.node = node;
		this.main = main;
		this.active = false;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Arrow) {
			if(node.getNodeId() == ((Arrow)other).node.getNodeId())
				return true;
		}
		return false;
	}
}
