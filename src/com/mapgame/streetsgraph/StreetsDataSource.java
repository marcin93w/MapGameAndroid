package com.mapgame.streetsgraph;

import java.util.ArrayList;

import org.json.JSONException;

import com.mapgame.streetsgraph.Way;

import jsqlite.Exception;
import jsqlite.Stmt;

public class StreetsDataSource extends SpatialiteDb {
	
	public Way getRandomWay() throws Exception, JSONException {
        Way way = null;
		String query = "SELECT oneway_fromto, oneway_tofrom, " + 
						"AsGeoJSON(geometry), node_from, node_to " +
        				"FROM roads " +
        				"LIMIT 1";
		
        Stmt stmt = db.prepare(query);
        if( stmt.step() ) {
            boolean forward = stmt.column_int(0) == 1 ? true : false;
            boolean backward = stmt.column_int(1) == 1 ? true : false;
            way = new Way(geoJSONToPointsList(stmt.column_string(2)), 
            		forward, backward,
            		stmt.column_int(3), stmt.column_int(4));
        }
        stmt.close();

        return way;
	}
	
	public ArrayList<CarPosition> getPossiblePositionsFromCrossroad(int crossroadNodeId) 
			throws Exception, JSONException {
		ArrayList<CarPosition> positions = new ArrayList<CarPosition>();
		String query = "SELECT oneway_fromto, oneway_tofrom, " + 
						"AsGeoJSON(geometry), node_from, node_to " +
						"FROM roads " +
						"WHERE (node_from = "+crossroadNodeId+" AND oneway_fromto = 1) OR " +
						"(node_to = "+crossroadNodeId+" AND oneway_tofrom = 1)";
		
		Stmt stmt = db.prepare(query);
        while( stmt.step() ) {
            boolean canForward = stmt.column_int(0) == 1 ? true : false;
            boolean canBackward = stmt.column_int(1) == 1 ? true : false;
            Way way = new Way(geoJSONToPointsList(stmt.column_string(2)), 
	            		canForward, canBackward,
	            		stmt.column_int(3), stmt.column_int(4));
            boolean backward = stmt.column_int(3) == crossroadNodeId ? false : true;
            positions.add(new CarPosition(way, backward));
        }
        stmt.close();
		
		return positions;
	}
	
}