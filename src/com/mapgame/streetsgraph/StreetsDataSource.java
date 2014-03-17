package com.mapgame.streetsgraph;

import com.mapgame.streetsgraph.Way;

import jsqlite.Stmt;

public class StreetsDataSource extends SpatialiteDb {
	
	public Way getRandomWay() {
        Way way = null;
		String query = "SELECT oneway_fromto, oneway_tofrom, AsGeoJSON(geometry) " +
        				"FROM roads " +
        				"LIMIT 1";
        try {
            Stmt stmt = db.prepare(query);
            if( stmt.step() ) {
                boolean forward = stmt.column_int(0) == 1 ? true : false;
                boolean backward = stmt.column_int(1) == 1 ? true : false;
                way = new Way(geoJSONToPointsList(stmt.column_string(2)), forward, backward);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return way;
	}
	
}
