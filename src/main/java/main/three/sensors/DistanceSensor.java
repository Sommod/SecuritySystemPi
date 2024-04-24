package main.three.sensors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.pi4j.context.Context;

public class DistanceSensor {
	private boolean isTimerOn;
	private Process p;

	private NoiseController alarm;
	private Temp run;

	public DistanceSensor(Context context, NoiseController alarm) {
		this.alarm = alarm;
		isTimerOn = false;
		
		try {
			p = Runtime.getRuntime().exec("python proximity.py");
		} catch(IOException e) { }
		
		run = new Temp();
	}

	public void shutdown() {
		p.destroy();
		run.run();
	}
	
	private class Temp implements Runnable {
		private float setupDistance;
		private float currentDistance;
		private boolean isSetup = false;
		
		@Override
		public void run() {
			try(BufferedReader reader = new BufferedReader(new FileReader(new File("distance_file.txt")))) {
				if(!isSetup) {
					setupDistance = Float.parseFloat(reader.readLine());
					isSetup = true;
				} else
					currentDistance = Float.parseFloat(reader.readLine());

				if(currentDistance <= setupDistance * .5F && !isTimerOn) {
					isTimerOn = true;
					initAlarm();
				}
				
				Thread.sleep(2000L);
			} catch(IOException | InterruptedException e) { }
		}

		private void initAlarm() {
			alarm.start();
		}
	}
}
