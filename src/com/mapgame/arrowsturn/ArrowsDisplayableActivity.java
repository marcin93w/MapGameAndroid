package com.mapgame.arrowsturn;

import android.view.View;

public interface ArrowsDisplayableActivity {
	public interface ViewRunnable {
		void run(View view);
	}
	
	void invokeArrowsView(ViewRunnable viewRunnable);
	
	void setStreetNameView(String text);
}
