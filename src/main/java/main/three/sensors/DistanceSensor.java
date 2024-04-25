package main.three.sensors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.pi4j.context.Context;

import main.three.AlarmSystem;

public class DistanceSensor {
	private Process p;

	private AlarmSystem alarm;

	public DistanceSensor(Context context, AlarmSystem alarm) {
		this.alarm = alarm;
		
//		try {
//			p = Runtime.getRuntime().exec("python proximity.py");
//		} catch(IOException e) { }
		
		//new Temp();
		
		alarm.start();
	}

	public void shutdown() {
		p.destroy();
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

				if(currentDistance <= setupDistance * .5F && !(alarm.isRunning() || alarm.isStarting())) {
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
