package com.mapgame.mapprojection;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.constants.GeoConstants;

import com.mapgame.mapprojection.MapViewManageableActivity.MapControllerRunable;
import com.mapgame.mapprojection.gamemap.GameMapCallback;
import com.mapgame.streetsgraph.model.DirectionVector;
import com.mapgame.streetsgraph.model.Point;

public abstract class AnimatedMap {
	public MapViewManageableActivity mapActivity;
	
	//for animation
	protected double moveStep;
	public Point position;
	
	protected final int moveTimeout = 40;
	
	public AnimatedMap(MapViewManageableActivity mapActivity) {
		this.mapActivity = mapActivity;
	}
	
	public class MoveAnimation extends Thread {
		protected Point end;
		protected GameMapCallback sender;

		double stepsCount;
		double stepLon, stepLat;
		
		volatile boolean stop = false;
		
		public MoveAnimation(Point end, GameMapCallback sender) {
			this.end = end;
			this.sender = sender;
			setMoveStep(moveStep);
		}
		
		public void setMoveStep(double moveStep) {
			this.stepsCount = (position.getLonLatDistance(end) / moveStep);
			if(this.stepsCount == 0)
				this.stepsCount = 1;
			this.stepLat = (end.getLatitudeE6() - position.getLatitudeE6()) / stepsCount;
			this.stepLon = (end.getLongitudeE6() - position.getLongitudeE6()) / stepsCount;
			//FIXME nie działa - prawdopodobnie trzeba wyliczac wektor przy kazdym przesunieciu punktu
			//DirectionVector v = new DirectionVector(position, end, 10d);
			//this.stepLon = v.getA() / GeoConstants.RADIUS_EARTH_METERS * 10E6;
			//this.stepLat = v.getB() / GeoConstants.RADIUS_EARTH_METERS * 10E6;
		}

		@Override
		public void run() {
			while(position.isBefore(end, stepLon > 0 ? false : true, stepLat > 0 ? false : true) 
					&& !stop) {
				mapActivity.invokeMapController(new MapControllerRunable() {
					@Override
					public void run(IMapController controller) {
						controller.setCenter(position);
					}
				});
				
				position.setLatitudeE6(position.getLatitudeE6() + (int)stepLat);
				position.setLongitudeE6(position.getLongitudeE6() + (int)stepLon);

				try {
					sleep(moveTimeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(!stop)
				sender.mapMoveFinished();
		}
		
		public void terminate() {
			stop = true;
		}
	}
	
}
