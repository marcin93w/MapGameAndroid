package com.mapgame.mapprojection;

import org.osmdroid.api.IMapController;

import com.mapgame.mapprojection.MapViewManageableActivity.MapControllerRunable;
import com.mapgame.mapprojection.gamemap.GameMapCallback;
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
		}

		@Override
		public void run() {
			while(position.isBefore(end, stepLon > 0 ? false : true, stepLat > 0 ? false : true)) {
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
			sender.mapMoveFinished();
		}
	}
	
}
