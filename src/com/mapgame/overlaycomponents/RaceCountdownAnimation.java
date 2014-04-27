package com.mapgame.overlaycomponents;

public class RaceCountdownAnimation extends Thread {
	
	public interface Callback {
		void raceCountdownFinished();
	}
	
	public interface CountdownActivity {
		void updateCountdownCounter(String text);
	}
	
	private Callback callback;
	private CountdownActivity activity;
	
	static final int countdownBase = 3;
	
	public RaceCountdownAnimation(Callback callback, 
			CountdownActivity activity) {
		super();
		this.callback = callback;
		this.activity = activity;
	}

	@Override
	public void run() {
		for(int i=countdownBase; i>0; i--) {
			activity.updateCountdownCounter(Integer.toString(i));
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		activity.updateCountdownCounter("GO");
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		callback.raceCountdownFinished();
	}
}
