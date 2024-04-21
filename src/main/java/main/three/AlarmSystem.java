package main.three;

public class AlarmSystem {
	private final short TIMER;
	private short countdownTimer;
	private Run run;
	
	public AlarmSystem() {
		TIMER = 30;
		countdownTimer = -1;
		run = new Run();
	}
	
	public void start() {
		countdownTimer = TIMER;
		run.reset();
		
		run.run();
	}
	
	public void end() {
		countdownTimer = -1;
		run.reset();
	}
	
	public boolean didAlarmSound() { return run.didTimerHitEnd(); }
	
	public boolean isRunning() { return countdownTimer > 0; }
	
	private class Run implements Runnable {
		
		private boolean hitEnd = false;

		@Override
		public void run() {
			while(countdownTimer > 0) {
				try { Thread.sleep(1000L); }
				catch(InterruptedException e) { e.printStackTrace(); }
				
				countdownTimer--;
				
				if(countdownTimer == 0)
					hitEnd = true;
			}
		}
		
		public boolean didTimerHitEnd() { return hitEnd; }
		public void reset() { hitEnd = false; }
	}
}
