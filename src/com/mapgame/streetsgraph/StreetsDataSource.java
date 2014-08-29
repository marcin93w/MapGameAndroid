package com.mapgame.streetsgraph;

import java.util.ArrayList;
import java.util.LinkedList;

import jsqlite.Exception;
import jsqlite.Stmt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mapgame.streetsgraph.model.CrossroadNode;
import com.mapgame.streetsgraph.model.Point;
import com.mapgame.streetsgraph.model.Road;
import com.mapgame.streetsgraph.model.Route;
import com.mapgame.streetsgraph.model.Way;

public class StreetsDataSource {

	private SpatialiteDb spatialite;
	
	public StreetsDataSource(Context context) {
		spatialite = new SpatialiteDb(context);
	}

	String possibleRoadsFromCrossroadQuery = "SELECT oneway_fromto, oneway_tofrom, "
			+ "AsGeoJSON(geometry), node_from, node_to, "
			+ "length, cost, "
			+ "name, class "
			+ "FROM roads " + "WHERE (node_from = $1" 
			+ " AND oneway_fromto = 1) OR " + "(node_to = $1"
			+ " AND oneway_tofrom = 1)";
	
	public ArrayList<Way> getPossibleWaysFromCrossroad(
			int crossroadNodeId) throws Exception, JSONException {
		ArrayList<Way> roads = new ArrayList<Way>();

		Stmt stmt = spatialite.getDb().prepare(possibleRoadsFromCrossroadQuery);
		stmt.bind(1, crossroadNodeId);
		
		while (stmt.step()) {
			boolean canForward = stmt.column_int(0) == 1 ? true : false;
			boolean canBackward = stmt.column_int(1) == 1 ? true : false;
			Road way = new Road(geoJSONToPointsArray(stmt.column_string(2)),
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
			stmt = spatialite.getDb().prepare(query);
		} else {
			String query = "SELECT node_id FROM roads_nodes "+
					//"WHERE PtDistWithin(geometry, MakePoint($1, $2), $3) = 0 "+
					//for debug:
					"WHERE PtDistWithin(geometry, MakePoint($1, $2), 0.025) = 1 " +
					"ORDER BY RANDOM() LIMIT 1";
			stmt = spatialite.getDb().prepare(query);
			stmt.bind(1, other.getLongitude());
			stmt.bind(2, other.getLatitude());
			//stmt.bind(3, minDistance);
		}
		
		CrossroadNode node = null;
		
		while(node == null) {		
			if(stmt.step()) {
				int nodeId = stmt.column_int(0);
				ArrayList<Way> ways = getPossibleWaysFromCrossroad(nodeId);
				if(ways.size() > 0) {
					for(Way w : ways) {
						if(!w.getRoad().isUnnamed()) {
							node = new CrossroadNode(w);
							break;
						}
					}
				}
			}
			if(node == null)
				stmt.reset();
			else
				stmt.close();
		}
		
		return node;
	}
	
	public Route getShortestRoute(CrossroadNode a, CrossroadNode b) 
			throws Exception, JSONException
	{
		LinkedList<Point> route = new LinkedList<Point>();
		double cost = 0, length = 0;
		
		String query = "SELECT Cost, AsGeoJSON(Geometry), GreatCircleLength(Geometry) " +
						"FROM roads_net " +
						"WHERE NodeFrom = $1 " +
						"AND NodeTo = $2 " +
						"LIMIT 1;";
		
		Stmt stmt = spatialite.getDb().prepare(query);
		stmt.bind(1, a.getNodeId());
		stmt.bind(2, b.getNodeId());
		if (stmt.step()) {
			route = geoJSONToPointsList(stmt.column_string(1));
			cost = stmt.column_double(0);
			length = stmt.column_double(2);
		}
		stmt.close();

		return new Route(route, cost, length);
	}
	
	//slowest method!
	protected ArrayList<Point> geoJSONToPointsArray(String json)
			throws JSONException {
		JSONObject way = new JSONObject(json);
		JSONArray geom = way.getJSONArray("coordinates");
		ArrayList<Point> points = new ArrayList<Point>();
		for (int j = 0; j < geom.length(); j++) {
			JSONArray lonLat = geom.getJSONArray(j);
			Point point = new Point(lonLat.optDouble(1), lonLat.getDouble(0));
			points.add(point);
		}
		return points;
	}
	
	protected LinkedList<Point> geoJSONToPointsList(String json)
			throws JSONException {
		JSONObject way = new JSONObject(json);
		JSONArray geom = way.getJSONArray("coordinates");
		LinkedList<Point> points = new LinkedList<Point>();
		for (int j = 0; j < geom.length(); j++) {
			JSONArray lonLat = geom.getJSONArray(j);
			Point point = new Point(lonLat.optDouble(1), lonLat.getDouble(0));
			points.add(point);
		}
		return points;
	}

}
