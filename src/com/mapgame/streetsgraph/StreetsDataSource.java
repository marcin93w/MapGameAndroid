package com.mapgame.streetsgraph;

import java.util.ArrayList;

import jsqlite.Exception;
import jsqlite.Stmt;

import org.json.JSONException;

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

}
