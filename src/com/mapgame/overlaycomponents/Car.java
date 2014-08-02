package com.mapgame.overlaycomponents;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;

import com.mapgame.R;

public class Car extends Component {
	
	double azimuth;
	Paint p;
	
	Bitmap b;
	
	public Car(Resources resources) {
		super(resources);
		p = new Paint();
		b = BitmapFactory.decodeResource(getResources(), R.drawable.car);
	}
	
	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}

	public void draw(Canvas canvas) {
		if(canvas != null) {
			int centerX = canvas.getWidth()/2;
			int centerY = canvas.getHeight()/2;
			
			canvas.drawColor(0, Mode.CLEAR);
	        int bitmapPositionX = centerX - b.getWidth()/2;
	        int bitmapPositionY = centerY - b.getHeight()/2;
	        float angle = (float)azimuth + 90;
	        canvas.save(Canvas.MATRIX_SAVE_FLAG);
	        canvas.rotate(angle, centerX, centerY);
	        canvas.drawBitmap(b, bitmapPositionX, bitmapPositionY, p);
	        canvas.restore();
		}
	}
	
}
