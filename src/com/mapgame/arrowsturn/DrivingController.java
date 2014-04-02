package com.mapgame.arrowsturn;

import java.util.ArrayList;

import jsqlite.Exception;

import org.json.JSONException;

import com.mapgame.streetsgraph.CrossroadNode;
import com.mapgame.streetsgraph.Road;
import com.mapgame.streetsgraph.StreetsDataSource;

public class DrivingController {
	final static double minTreeLength = 2;
	
	CrossroadNode treeRoot;
	TurnArrows arrowsManager;
	StreetsDataSource sds;
	
	public DrivingController(TurnArrows arrowsManager, StreetsDataSource sds) {
		this.arrowsManager = arrowsManager;
		this.sds = sds;
	}
	
	public void initialize(Road initialRoad) {
		treeRoot = new CrossroadNode(initialRoad);
		addChildrenToNodeRecursive(treeRoot, minTreeLength);
		updateArrows();
	}
	
	public Road getNextRoad() {
		Road nextRoad = treeRoot.getSelectedChild().getRoad();
		updateTree(nextRoad);
		updateArrows();
		return nextRoad;
	}

	private void updateTree(Road nextRoad) {
		treeRoot = treeRoot.getSelectedChild();
		addChildrenToNodeRecursive(treeRoot, minTreeLength);
	}

	private void addChildrenToNodeRecursive(CrossroadNode node, double length) {
		addChildrenToNode(node);
		for(CrossroadNode child : node.getChildren()) {
			double roadToChildLength = child.getRoad().getWay().getLength();
			if(roadToChildLength < length) {
				addChildrenToNodeRecursive(child, length - roadToChildLength);
			}
		}
	}
	
	private void addChildrenToNode(CrossroadNode node) {
		if(node.getChildren() == null) {
			ArrayList<Road> childRoads = null;
			try {
				childRoads = sds.getPossibleRoadsFromCrossroad(node.getNodeId());
			} catch (Exception e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			node.addChildren(childRoads);
		}
	}
	
	private void updateArrows() {
		arrowsManager.clearArrows();
		for(CrossroadNode node : treeRoot.getChildren()) {
			addArrowsForNodeRecursive(node, 
					node == treeRoot.getSelectedChild() ? true : false);
		}
	}
	
	private void addArrowsForNodeRecursive(CrossroadNode node, boolean main) {
		if(node.getChildren() == null) {
			arrowsManager.addArrow(node, main);
		} else {
			if(main) {
				for(CrossroadNode childNode : node.getChildren()) {
					addArrowsForNodeRecursive(childNode,
							childNode == node.getSelectedChild() ? true : false);
				} 
			} else {
				for(CrossroadNode childNode : node.getChildren()) {
					addArrowsForNodeRecursive(childNode, false);
				}
			}
		}
	}
	
}
