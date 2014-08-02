package com.mapgame.arrowsturn;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapgame.R;
import com.mapgame.arrowsturn.ArrowsDisplayableActivity.ViewRunnable;
import com.mapgame.streetsgraph.model.DirectionVector;
import com.mapgame.streetsgraph.model.Way.Position;

public class TurnArrows {
	ArrowsDisplayableActivity mainActivity;
	Context context;

	class ImageArrow {
		public ImageView image;
		public Arrow arrow;
		public ImageArrow(ImageView image, Arrow arrow) {
			this.image = image;
			this.arrow = arrow;
		}
	}
	
	ArrayList<ImageArrow> arrows, arrowsTmp;

	Semaphore s;

	public final static int distanceFromCenter = 90;
	
	final int arrowsZeroZIndex = 1;

	public TurnArrows(ArrowsDisplayableActivity activity, Context context) {
		this.mainActivity = activity;
		this.context = context;
		arrows = new ArrayList<ImageArrow>();

		s = new Semaphore(1);
	}
	
	public void addArrowsBegin() {
		arrowsTmp = new ArrayList<TurnArrows.ImageArrow>();
	}
	
	public void addArrow(final Arrow arrow) {
		double azimuth = arrow.node.getWay().getAzimuth(Position.START);
		final ImageView imageView = new ImageView(context);
		if (arrow.main) {
			imageView.setImageResource(R.drawable.arrow_selected);
		}
		else {
			imageView.setImageResource(getProperImage(arrow));
		}
		
		rotateArrow(imageView, (float)azimuth - 90);
		locateArrow(imageView, new DirectionVector(azimuth, distanceFromCenter), arrow.main);

		imageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				s.acquireUninterruptibly();
				for (ImageArrow arrowImage : arrows) {
					if(arrowImage.image != imageView) {
						arrowImage.image.setImageResource(getProperImage(arrowImage.arrow));
						arrowImage.image.bringToFront();
					}
				}
				s.release();
				imageView.setImageResource(R.drawable.arrow_selected);
				mainActivity.getStreetNameView().setText(arrow.node.getWay().getRoad().getName());
				arrow.node.select();
				return false;
			}
		});

		arrowsTmp.add(new ImageArrow(imageView, arrow));
	}
	
	public void addArrowsEnd() {
		//FIXME jeśli najpierw dodajemy strzałki a potem je usuwamy 
		//to w przypadku tej samej strzałki jest exception że ma już rodzica
		s.acquireUninterruptibly();
		for(final ImageArrow ia : arrowsTmp) {
			if(ia.arrow.main) {
				mainActivity.invokeNextStreetView(new ViewRunnable() {
					@Override
					public void run(View streetNameView) {
						((TextView) streetNameView).setText(
								ia.arrow.node.getWay().getRoad().getName());
					}		
				});
				
				mainActivity.invokeArrowsView(new ViewRunnable() {
					@Override
					public void run(View arrowsView) {
						((ViewGroup) arrowsView).addView(ia.image, arrowsZeroZIndex-1);				
					}
				});
				
			} else {
				mainActivity.invokeArrowsView(new ViewRunnable() {
					@Override
					public void run(View arrowsView) {
						((ViewGroup) arrowsView).addView(ia.image, arrowsZeroZIndex);				
					}
				});
			}
		}
		
		clearArrows();
		arrows = arrowsTmp;
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

	private void locateArrow(final ImageView view, final DirectionVector vector, boolean main) {
		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ABOVE, R.id.fakeView);
		params.addRule(RelativeLayout.RIGHT_OF, R.id.fakeView);

		vector.scaleToMagnitude(distanceFromCenter);
		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
		    public boolean onPreDraw() {
		    	view.getViewTreeObserver().removeOnPreDrawListener(this);
		        params.setMargins((int)dpToPx((float)vector.getX()) - view.getWidth()/2, 
		        		0, 0, 
		        		(int)dpToPx((float)vector.getY()) - view.getHeight()/2);
				view.setLayoutParams(params);
		        return true;
		    }
		});
	}

	private void rotateArrow(final ImageView imageView, final float angle) {
		ViewTreeObserver vto = imageView.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
		    public boolean onPreDraw() {
				Matrix matrix = new Matrix();
				imageView.setScaleType(ScaleType.MATRIX);
				matrix.preRotate(angle, imageView.getWidth()/2, imageView.getHeight()/2);
				imageView.setImageMatrix(matrix);
				return true;
		    }
		});
	}

	private float dpToPx(float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, 
				context.getResources().getDisplayMetrics());
	}
	
	public void clearArrows() {
		for (final ImageArrow arrow : arrows) {
			mainActivity.invokeArrowsView(new ViewRunnable() {
				@Override
				public void run(View view) {
					((ViewGroup) view).removeView(arrow.image);
				}
			});		
		}
	}
}
