package com.mapgame.overlaycomponents;

import com.mapgame.engine.DirectionVector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;

public class DirectionArrow extends Component {
	
	DirectionVector vector;
	Paint p;
	
	public DirectionArrow() {
		p = new Paint();
	}
	
	public void setVector(DirectionVector vector) {
		this.vector = vector;
	}

	public void draw(Canvas canvas) {
		if(vector != null) {
			int centerX = canvas.getWidth()/2;
			int centerY = canvas.getHeight()/2;
			
			p.setColor(Color.RED);
			vector.scaleToMagnitude(100);
			
			canvas.drawColor(0, Mode.CLEAR);
			canvas.drawLine(centerX, centerY, 
					centerX + (int)vector.getA(), 
					centerY - (int)vector.getB() , p);
		}
	}
	
}
