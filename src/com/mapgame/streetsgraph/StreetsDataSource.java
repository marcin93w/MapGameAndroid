package com.mapgame.streetsgraph;

import java.util.ArrayList;
import java.util.Random;

import jsqlite.Exception;
import jsqlite.Stmt;

import org.json.JSONException;

import com.mapgame.streetsgraph.model.CrossroadNode;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Road;
import com.mapgame.streetsgraph.model.Way;

public class StreetsDataSource extends SpatialiteDb {

	public Road getRandomWay() throws Exception, JSONException {
		Road way = null;
		String query = "SELECT oneway_fromto, oneway_tofrom, "
				+ "AsGeoJSON(geometry), node_from, node_to, " 
				+ "length, cost, "
				+ "name, class "
				+ "FROM roads " + "LIMIT 1";

		Stmt stmt = db.prepare(query);
		if (stmt.step()) {
			boolean forward = stmt.column_int(0) == 1 ? true : false;
			boolean backward = stmt.column_int(1) == 1 ? true : false;
			way = new Road(geoJSONToPointsList(stmt.column_string(2)), forward,
					backward, stmt.column_int(3), stmt.column_int(4),
					stmt.column_double(5), stmt.column_double(6),
					stmt.column_string(7), stmt.column_string(8));
		}
		stmt.close();

		return way;
	}
	
	public Road getWay(String name) throws Exception, JSONException {
		Road way = null;
		String query = "SELECT oneway_fromto, oneway_tofrom, "
				+ "AsGeoJSON(geometry), node_from, node_to, " 
				+ "length, cost, "
				+ "name, class "
				+ "FROM roads "
				+ "WHERE name='" + name  +  "' "
				+ "LIMIT 1";

		Stmt stmt = db.prepare(query);
		if (stmt.step()) {
			boolean forward = stmt.column_int(0) == 1 ? true : false;
			boolean backward = stmt.column_int(1) == 1 ? true : false;
			way = new Road(geoJSONToPointsList(stmt.column_string(2)), forward,
					backward, stmt.column_int(3), stmt.column_int(4),
					stmt.column_double(5), stmt.column_double(6),
					stmt.column_string(7), stmt.column_string(8));
		}
		stmt.close();

		return way;
	}

	String possibleRoadsFromCrossroadQuery = "SELECT oneway_fromto, oneway_tofrom, "
			+ "AsGeoJSON(geometry), node_from, node_to, "
			+ "length, cost, "
			+ "name, class "
			+ "FROM roads " + "WHERE (node_from = $1" 
			+ " AND oneway_fromto = 1) OR " + "(node_to = $1"
			+ " AND oneway_tofrom = 1)";
	
	public ArrayList<Way> getPossibleRoadsFromCrossroad(
			int crossroadNodeId) throws Exception, JSONException {
		ArrayList<Way> roads = new ArrayList<Way>();

		Stmt stmt = db.prepare(possibleRoadsFromCrossroadQuery);
		stmt.bind(1, crossroadNodeId);
		
		while (stmt.step()) {
			boolean canForward = stmt.column_int(0) == 1 ? true : false;
			boolean canBackward = stmt.column_int(1) == 1 ? true : false;
			Road way = new Road(geoJSONToPointsList(stmt.column_string(2)),
					canForward, canBackward, stmt.column_int(3),
					stmt.column_int(4), stmt.column_double(5),
					stmt.column_double(6), stmt.column_string(7), stmt.column_string(8));
			boolean backward = stmt.column_int(3) == crossroadNodeId ? false
					: true;
			roads.add(new Way(way, backward));
		}
		stmt.close();

		return roads;
	}
	
	/*
	 * Used for get a start point (initial tree root)
	 */
	public CrossroadNode getRandomCrossroadNode() throws Exception, JSONException {
		return getRandomCrossroadNode(null, 0);
	}
	
	/*
	 * Used to get end point
	 */
	public CrossroadNode getRandomCrossroadNode(Point other, double minDistance) 
			throws Exception, JSONException {
		Stmt stmt;
		if(other == null || minDistance == 0) {
			String query = "SELECT node_id FROM roads_nodes ORDER BY RANDOM() LIMIT 1";
			stmt = db.prepare(query);
		} else {
			String query = "SELECT node_id FROM roads_nodes "+
					//"WHERE PtDistWithin(geometry, MakePoint($1, $2), $3) = 0 "+
					//for debug:
					"WHERE PtDistWithin(geometry, MakePoint($1, $2), 0.05) = 1 "+
					"ORDER BY RANDOM() LIMIT 1";
			stmt = db.prepare(query);
			stmt.bind(1, other.getLongitude());
			stmt.bind(2, other.getLatitude());
			//stmt.bind(3, minDistance);
		}
		
		CrossroadNode node = null;
		Random rand = new Random();
		
		while(node == null) {		
			if(stmt.step()) {
				int nodeId = stmt.column_int(0);
				ArrayList<Way> ways = getPossibleRoadsFromCrossroad(nodeId);
				if(ways.size() > 0) {
					node = new CrossroadNode(ways.get(rand.nextInt(ways.size())));
				}
			}
			if(node == null)
				stmt.reset();
			else
				stmt.close();
		}
		
		return node;
	}

}
