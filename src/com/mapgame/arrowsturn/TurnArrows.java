package com.mapgame.arrowsturn;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapgame.R;
import com.mapgame.engine.DirectionVector;
import com.mapgame.streetsgraph.Way;

public class TurnArrows {
	Activity mainActivity;
	RelativeLayout view;
	TextView streetNameView;

	class ImageArrow {
		public ImageView image;
		public Arrow arrow;
		public ImageArrow(ImageView image, Arrow arrow) {
			this.image = image;
			this.arrow = arrow;
		}
	}
	
	ArrayList<ImageArrow> arrows;

	Semaphore s;

	final int distanceFromCenter = 150;
	final int imageWidth = 256;
	final int imageHeight = 256;
	int halfWidthInDp, halfHeightInDp;

	public TurnArrows(Activity mainActivity, RelativeLayout view, TextView streetNameView) {
		this.mainActivity = mainActivity;
		this.view = view;
		this.streetNameView = streetNameView;
		arrows = new ArrayList<ImageArrow>();

		s = new Semaphore(1);

		DisplayMetrics displayMetrics = mainActivity.getApplicationContext()
				.getResources().getDisplayMetrics();

		halfWidthInDp = Math.round(imageWidth / 2
				/ (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		halfHeightInDp = Math.round(imageHeight / 2
				/ (displayMetrics.ydpi / DisplayMetrics.DENSITY_DEFAULT));
	}
	
	public void addArrow(final Arrow arrow) {
		DirectionVector vector = arrow.node.getWay().getDirectionVector(Way.Position.START);
		final ImageView imageView = new ImageView(
				mainActivity.getApplicationContext());
		if (arrow.main) {
			imageView.setImageResource(R.drawable.arrow_selected);
		}
		else {
			imageView.setImageResource(getProperImage(arrow));
		}
		
		rotateArrow(imageView,
				(float) vector.getAngleInDegrees(new DirectionVector(1, 0)));
		locateArrow(imageView, vector);

		imageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				s.acquireUninterruptibly();
				for (ImageArrow arrowImage : arrows) {
					arrowImage.image.setImageResource(getProperImage(arrowImage.arrow));
				}
				s.release();
				imageView.setImageResource(R.drawable.arrow_selected);
				streetNameView.setText(arrow.node.getWay().getRoad().getName());
				arrow.node.select();
				return false;
			}
		});

		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				view.addView(imageView);
				if(arrow.main)
					streetNameView.setText(arrow.node.getWay().getRoad().getName());
			}
		});

		s.acquireUninterruptibly();
		arrows.add(new ImageArrow(imageView, arrow));
		s.release();
	}
	
	private int getProperImage(Arrow arrow) {
		switch(arrow.node.getWay().getRoad().getRoadClass()) {
			case MOTORWAY:
			case MOTORWAY_LINK:
				return R.drawable.arrow_blue;
			case PRIMARY:
			case PRIMARY_LINK:
				return R.drawable.arrow_red;
			case SECONDARY:
			case SECONDARY_LINK:
				return R.drawable.arrow_orange;
			case TERTIARY:
			case TERTIARY_LINK:
			case TRUNK:
			case TRUNK_LINK:
				return R.drawable.arrow_yellow;
			default:
				return R.drawable.arrow;
		}
	}

	private void locateArrow(ImageView view, DirectionVector vector) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.RIGHT_OF, R.id.fakeView);
		params.addRule(RelativeLayout.ABOVE, R.id.fakeView);

		vector.scaleToMagnitude(distanceFromCenter);
		params.setMargins((int) vector.getA() - halfWidthInDp, 0, 0,
				(int) vector.getB() - halfHeightInDp);

		view.setLayoutParams(params);
	}

	private void rotateArrow(ImageView imageView, float angle) {
		Matrix matrix = new Matrix();
		imageView.setScaleType(ScaleType.MATRIX);
		matrix.preRotate(angle, halfWidthInDp, halfHeightInDp);
		imageView.setImageMatrix(matrix);
	}

	public void clearArrows() {
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				s.acquireUninterruptibly();
				for (ImageArrow arrow : arrows)
					view.removeView(arrow.image);
				s.release();
			}
		});
	}
}
