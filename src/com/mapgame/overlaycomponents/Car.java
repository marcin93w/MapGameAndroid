package com.mapgame.overlaycomponents;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;

import com.mapgame.R;
import com.mapgame.streetsgraph.model.DirectionVector;

public class Car extends Component {
	
	DirectionVector vector;
	Paint p;
	
	Bitmap b;
	
	public Car(Resources resources) {
		super(resources);
		p = new Paint();
		b = BitmapFactory.decodeResource(getResources(), R.drawable.car);
	}
	
	public void setVector(DirectionVector vector) {
		this.vector = vector;
	}

	public void draw(Canvas canvas) {
		if(vector != null) {
			int centerX = canvas.getWidth()/2;
			int centerY = canvas.getHeight()/2;
			
			canvas.drawColor(0, Mode.CLEAR);
	        int bitmapPositionX = centerX - b.getWidth()/2;
	        int bitmapPositionY = centerY - b.getHeight()/2;
	        float angle = (float)vector.getAngleInDegrees(new DirectionVector(-1, 0));
	        canvas.save(Canvas.MATRIX_SAVE_FLAG);
	        canvas.rotate(-angle, centerX, centerY);
	        canvas.drawBitmap(b, bitmapPositionX, bitmapPositionY, p);
	        canvas.restore();
		}
	}
	
}
