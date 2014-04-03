package com.mapgame.arrowsturn;

import java.util.ArrayList;

import jsqlite.Exception;

import org.json.JSONException;

import com.mapgame.streetsgraph.CrossroadNode;
import com.mapgame.streetsgraph.Road;
import com.mapgame.streetsgraph.StreetsDataSource;

public class DrivingController {
	final static double minTreeLength = 100;
	
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
		treeRoot = treeRoot.getSelectedChild().separate();
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
	
	private ArrayList<Arrow> arrows;
	
	private void updateArrows() {
		arrowsManager.clearArrows();
		arrows = new ArrayList<Arrow>();
		for(CrossroadNode node : treeRoot.getChildren()) {
			addArrowsForNodeRecursive(new Arrow(node, 
					node == treeRoot.getSelectedChild() ? true : false));
		}
		for(Arrow arrow : arrows) {
			if(arrow.active)
				arrowsManager.addArrow(arrow);
		}
	}
	
	private void addArrowsForNodeRecursive(Arrow arrow) {
		if(!arrows.contains(arrow)) {
			arrows.add(arrow);
			if(arrow.node.getChildren() == null) {		
				arrow.setActive(true);
			} else {
				if(!treeRoot.isReturnToParent(arrow.node)) {
					if(arrow.main) {
						for(CrossroadNode childNode : arrow.node.getChildren()) {
							addArrowsForNodeRecursive(new Arrow(childNode,
									childNode == arrow.node.getSelectedChild() ? true : false));
						} 
					} else {
						for(CrossroadNode childNode : arrow.node.getChildren()) {
							addArrowsForNodeRecursive(new Arrow(childNode, false));
						}
					}
				}
			}
		} else {
			if(arrows.get(arrows.indexOf(arrow)).node.getLevel() > 
					arrow.node.getLevel()) {
				arrows.remove(arrow);
				addArrowsForNodeRecursive(arrow);
			}
		}
	}
	
}
