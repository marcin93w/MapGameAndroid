package com.mapgame.streetsgraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataLoader {
	String url = "http://192.168.43.200/~me/MapGame/ajaxGate.php?getWays";
	
	public List<Way> loadData() throws DataLoadException {
        InputStream inputStream = null;
        String result = "";
        JSONArray jArray;
        
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
 
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                throw new DataLoadException("InputStream is null");
 
        } catch (Exception e) {
            throw new DataLoadException(e);
        }
        
        try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			throw new DataLoadException(e);
		}
        
        return parseStreetsData(jArray);
    }
 
    private List<Way> parseStreetsData(JSONArray jArray) throws DataLoadException {
		ArrayList<Way> ways = new ArrayList<Way>();
    	
    	for(int i=0; i<jArray.length(); i++) {
			try {
				JSONObject jObj = jArray.getJSONObject(i);
				
				JSONArray geom = jObj.getJSONArray("geom");
				ArrayList<Point> points = new ArrayList<Point>();
				for(int j=0; j<geom.length(); j++) {
					JSONArray lonLat = geom.getJSONArray(j);
					Point point = new Point(lonLat.optDouble(1), lonLat.getDouble(0));
					points.add(point);
				}
				
				ways.add(new Way(points, jObj.getBoolean("oneway")));
			} catch (JSONException e) {
				throw new DataLoadException(e);
			}
		}
    	
    	assignNeighbors(ways, jArray);
    	   	
		return ways;
	}

    private void assignNeighbors(List<Way> ways, JSONArray jArray) throws DataLoadException {
    	for(int i=0; i<jArray.length(); i++) {
    		try {
				JSONObject jObj = jArray.getJSONObject(i);
	    		
				ArrayList<Way> prevBackward = new ArrayList<Way>(); 
				try {
					JSONArray jsonPrevBackward = jObj.getJSONArray("prevBackward");				
					for(int j=0; j<jsonPrevBackward.length(); j++) {
		                prevBackward.add(ways.get(jsonPrevBackward.getInt(j)));
					}
				} catch (JSONException e) {
					//nie ma pola w jsonie wiec ta lista sasiadow jest pusta
				}
				ways.get(i).setPrevBackward(prevBackward);
				
				ArrayList<Way> prevForward = new ArrayList<Way>(); 
				try {
					JSONArray jsonPrevForward = jObj.getJSONArray("prevForward");				
					for(int j=0; j<jsonPrevForward.length(); j++) {
		                prevForward.add(ways.get(jsonPrevForward.getInt(j)));
					}
				} catch (JSONException e) {
					//nie ma pola w jsonie wiec ta lista sasiadow jest pusta
				}
				ways.get(i).setPrevForward(prevForward);
				
				ArrayList<Way> nextBackward = new ArrayList<Way>(); 
				try {
					JSONArray jsonNextBackward = jObj.getJSONArray("nextBackward");				
					for(int j=0; j<jsonNextBackward.length(); j++) {
		                nextBackward.add(ways.get(jsonNextBackward.getInt(j)));
					}
				} catch (JSONException e) {
					//nie ma pola w jsonie wiec ta lista sasiadow jest pusta
				}
				ways.get(i).setNextBackward(nextBackward);
				
				ArrayList<Way> nextForward = new ArrayList<Way>(); 
				try {
					JSONArray jsonNextForward = jObj.getJSONArray("nextForward");				
					for(int j=0; j<jsonNextForward.length(); j++) {
		                nextForward.add(ways.get(jsonNextForward.getInt(j)));
		            }
				} catch (JSONException e) {
					//nie ma pola w jsonie wiec ta lista sasiadow jest pusta
				}
				ways.get(i).setNextForward(nextForward);
				

    		} catch (JSONException e) {
				throw new DataLoadException(e);
			}
    	}
    }

	private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result; 
    }
}
