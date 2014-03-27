package com.mapgame.arrowsturn;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.mapgame.R;
import com.mapgame.engine.DirectionVector;

public class TurnArrows {
	Activity mainActivity;
	RelativeLayout view;
	
	ArrayList<ImageView> arrows;
	
	final int distanceFromCenter = 200;
	final int imageWidth = 256;
	final int imageHeight = 256;
	int halfWidthInDp, halfHeightInDp;
	
	public TurnArrows(Activity mainActivity, RelativeLayout view) {
		this.mainActivity = mainActivity;
		this.view = view;
		arrows = new ArrayList<ImageView>();
		halfWidthInDp = convertPixelsToDp(imageWidth/2);
		halfHeightInDp = convertPixelsToDp(imageHeight/2);
	}
	 
	public void setArrow(DirectionVector vector) {
        final ImageView imageView = new ImageView(mainActivity.getApplicationContext());
        imageView.setImageResource(R.drawable.transparrent_arrow);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.fakeView);
        params.addRule(RelativeLayout.ABOVE, R.id.fakeView);
        
        vector.scaleToMagnitude(distanceFromCenter);
        params.setMargins((int)vector.getA()-halfWidthInDp, 0, 0,
        		(int)vector.getB()-halfHeightInDp);

        //vector [1,0] is default image Arrow vector
        rotate(imageView, (float)vector.getAngleInDegrees(new DirectionVector(1, 0)));
        
        imageView.setLayoutParams(params);
        
        mainActivity.runOnUiThread(new Runnable() {			
			@Override
			public void run() {
		        view.addView(imageView);			
			}
		});
        arrows.add(imageView);
	}
	
	private void rotate(ImageView imageView, float angle) {
		Matrix matrix=new Matrix();
		imageView.setScaleType(ScaleType.MATRIX);   //required
		matrix.postRotate(angle, halfWidthInDp, halfHeightInDp);
		imageView.setImageMatrix(matrix);
	}
	
	public void clearArrows() {
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for(View arrow : arrows)
					view.removeView(arrow);
			}
		});
	}
	
	private int convertPixelsToDp(float px){
	    Resources resources = mainActivity.getApplicationContext().getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    int dp = (int) (px / (metrics.densityDpi / 160f));
	    return dp;
	}
}
