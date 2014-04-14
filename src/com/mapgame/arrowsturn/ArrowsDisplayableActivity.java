package com.mapgame.arrowsturn;

import android.view.View;
import android.widget.TextView;

public interface ArrowsDisplayableActivity {
	public interface ViewRunnable {
		void run(View view);
	}
	
	void invokeArrowsView(ViewRunnable viewRunnable);
	void invokeNextStreetView(ViewRunnable job);
	
	TextView getStreetNameView();
}
