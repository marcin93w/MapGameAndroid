package com.mapgame.arrowsturn;

import com.mapgame.streetsgraph.model.CrossroadNode;

public class Arrow {
	public CrossroadNode node;
	public boolean main;
	public boolean active; //arrows replaced by child arrows are marked as non active
	
	public Arrow(CrossroadNode node) {
		this.node = node;
		this.active = false;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setMain(boolean main) {
		this.main = main;
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
