package com.mapgame.arrowsturn;

import java.util.ArrayList;

import jsqlite.Exception;

import org.json.JSONException;

import com.mapgame.streetsgraph.CrossroadNode;
import com.mapgame.streetsgraph.StreetsDataSource;
import com.mapgame.streetsgraph.Way;

public class DrivingController {
	final static double minTreeLengthForMainRoads = 100;
	final static double minTreeLengthForNormalRoads = 30;
	
	CrossroadNode treeRoot;
	TurnArrows arrowsManager;
	StreetsDataSource sds;
	
	public DrivingController(TurnArrows arrowsManager, StreetsDataSource sds) {
		this.arrowsManager = arrowsManager;
		this.sds = sds;
	}
	
	public void initialize(Way initialRoad) {
		treeRoot = new CrossroadNode(initialRoad);
		addChildrenToNodeRecursive(treeRoot, minTreeLengthForMainRoads);
		updateArrows();
	}
	
	public Way getNextRoad() {
		Way nextRoad = treeRoot.getSelectedChild().getWay();
		updateTree(nextRoad);
		updateArrows();
		return nextRoad;
	}

	private void updateTree(Way nextRoad) {
		treeRoot = treeRoot.getSelectedChild().separate();
		addChildrenToNodeRecursive(treeRoot, minTreeLengthForMainRoads);
	}

	private void addChildrenToNodeRecursive(CrossroadNode node, double length) {
		addChildrenToNode(node);
		for(CrossroadNode child : node.getChildren()) {
			double roadToChildLength = child.getWay().getRoad().getLength();
			double allowedLength = length;
			if(!child.getWay().getRoad().isMainRoad()) {
				allowedLength -= minTreeLengthForMainRoads - minTreeLengthForNormalRoads;
			}
			
			if(roadToChildLength < allowedLength) {
				addChildrenToNodeRecursive(child, length - roadToChildLength);
			}
		}
	}
	
	private void addChildrenToNode(CrossroadNode node) {
		if(node.getChildren() == null) {
			ArrayList<Way> childRoads = null;
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
			addArrowsForNodeRecursive(new Arrow(node));
		}
		markMainArrow();
		for(Arrow arrow : arrows) {
			if(arrow.active)
				arrowsManager.addArrow(arrow);
		}
	}
	
	private void addArrowsForNodeRecursive(Arrow arrow) {
		if(!arrows.contains(arrow)) {
			arrows.add(arrow);
			if(arrow.node.getChildren() == null || treeRoot.isReturnToParent(arrow.node)) {
				arrow.setActive(true);
			} else {
				for(CrossroadNode childNode : arrow.node.getChildren()) {
					addArrowsForNodeRecursive(new Arrow(childNode));
				} 
			}
		} else {
			Arrow currentArrow = arrows.get(arrows.indexOf(arrow));
			//FIXME Kapelanka nie przechodzi warunku leveli
			//być może krótszy level pokazuje na dalszy node i dlatego wyswietlaja sie 2
			if(currentArrow.node.getLevel() > arrow.node.getLevel()) {
				arrows.remove(currentArrow);
				addArrowsForNodeRecursive(arrow);
			}
		}
	}
	
	private void markMainArrow() {
		CrossroadNode lastSelectedNode = treeRoot;
		while(lastSelectedNode.getSelectedChild() != null) {
			lastSelectedNode = lastSelectedNode.getSelectedChild();
		}
		
		while(lastSelectedNode != null) {
			for(Arrow arrow : arrows) {
				if(arrow.active) {
					if(lastSelectedNode.haveInAncestry(arrow.node)) {
						arrow.main = true;
						return;
					}
				}
			}
			lastSelectedNode = lastSelectedNode.getParent();
		}
	}
}
