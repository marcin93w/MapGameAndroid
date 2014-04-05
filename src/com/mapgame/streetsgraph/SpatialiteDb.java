package com.mapgame.streetsgraph;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mapgame.streetsgraph.Point;

import jsqlite.Database;

public class SpatialiteDb {

	protected Database db;

	public SpatialiteDb() {
		try {
			String sdcardDir = "/mnt/ext_card/spatialite/";
			File spatialDbFile = new File(sdcardDir + "dbb.sqlite");

			db = new jsqlite.Database();
			db.open(spatialDbFile.getAbsolutePath(),
					jsqlite.Constants.SQLITE_OPEN_READWRITE | jsqlite.Constants.SQLITE_OPEN_CREATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//slowest method!
	protected ArrayList<Point> geoJSONToPointsList(String json)
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

}
