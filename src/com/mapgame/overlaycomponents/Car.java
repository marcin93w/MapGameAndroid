package com.mapgame.overlaycomponents;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;

import com.mapgame.R;
import com.mapgame.engine.DirectionVector;

public class Car extends Component {
	
	DirectionVector vector;
	Paint p;
	
	public Car(Resources resources) {
		super(resources);
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
			p=new Paint();
	        Bitmap b=BitmapFactory.decodeResource(getResources(), R.drawable.car);
	        int bitmapPositionX = centerX - b.getWidth()/2;
	        int bitmapPositionY = centerY - b.getHeight()/2;
	        canvas.save(Canvas.MATRIX_SAVE_FLAG);
	        canvas.rotate((float)-vector.getAngleInDegrees(new DirectionVector(-1, 0)), 
	        		centerX, centerY);
	        canvas.drawBitmap(b, bitmapPositionX, bitmapPositionY, p);
	        canvas.restore();
		}
	}
	
}
