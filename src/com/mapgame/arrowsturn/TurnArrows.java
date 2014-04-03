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

import com.mapgame.R;
import com.mapgame.engine.DirectionVector;
import com.mapgame.streetsgraph.Road;

public class TurnArrows {
	Activity mainActivity;
	RelativeLayout view;

	ArrayList<ImageView> arrows;

	Semaphore s;

	final int distanceFromCenter = 150;
	final int imageWidth = 256;
	final int imageHeight = 256;
	int halfWidthInDp, halfHeightInDp;

	public TurnArrows(Activity mainActivity, RelativeLayout view) {
		this.mainActivity = mainActivity;
		this.view = view;
		arrows = new ArrayList<ImageView>();

		s = new Semaphore(1);

		DisplayMetrics displayMetrics = mainActivity.getApplicationContext()
				.getResources().getDisplayMetrics();

		halfWidthInDp = Math.round(imageWidth / 2
				/ (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		halfHeightInDp = Math.round(imageHeight / 2
				/ (displayMetrics.ydpi / DisplayMetrics.DENSITY_DEFAULT));
	}
	
	public void addArrow(final Arrow arrow) {
		DirectionVector vector = arrow.node.getRoad().getDirectionVector(Road.Position.START);
		final ImageView imageView = new ImageView(
				mainActivity.getApplicationContext());
		if (arrow.main)
			imageView.setImageResource(R.drawable.transparrent_arrow_framea);
		else
			imageView.setImageResource(R.drawable.transparrent_arrow);

		locateArrow(imageView, vector);
		rotateArrow(imageView,
				(float) vector.getAngleInDegrees(new DirectionVector(1, 0)));

		imageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				s.acquireUninterruptibly();
				for (ImageView arrow : arrows) {
					arrow.setImageResource(R.drawable.transparrent_arrow);
				}
				s.release();
				imageView.setImageResource(R.drawable.transparrent_arrow_framea);
				arrow.node.select();
				return false;
			}
		});

		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				view.addView(imageView);
			}
		});

		s.acquireUninterruptibly();
		arrows.add(imageView);
		s.release();
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
				for (View arrow : arrows)
					view.removeView(arrow);
				s.release();
			}
		});
	}
}
