package main.three.sensors;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;

public class DistanceSensor {
/*	
	private final int PINS[] = {4, 17}; //Trigger, Echo
	private DigitalInput input;
	private DigitalOutput output;
	private long time;
	
	public DistanceSensor(Context context) {
		DigitalOutputConfigBuilder outputBuilder = DigialOutput.newConfigBuilder(context).id("dist").address(PINS[0]).provider("pigpio-digital-output");
		output = context.create(outputBuilder);

		DigitalInputConfigBuilder inputBuilder = DigitalInput.newConfigBuilder(context).id("power").address(PINS[1]).provider("pigpio-digital-input");
		input = context.create(inputBuilder);

	}

	private class Temp implements DigitalStateChangeEvent {
		@SuppressWarnings("rawtypes")
		@Override
		public void onDigitalStateChange(DigitalStateChangeEvent event) {
			if(event.state() == DigitalState.LOW) {
				
			}
		}
	}
*/
	private boolean isTimerOn;
	private Process p;

	private NoiseController alarm;

	public DistanceSensor(Context context, AlarmSystem alarm) {
		this.alarm = alarm;
		isTimerOn = false;
		
		p = Runtime.newProcess("python proximity.py"); //TODO: Need to add Shutdown process after THIS program shuts down
		Thread.sleep(3000L);
	}

	public void shutdown() {
		p.shutdown();
	}

	
	private class Run implements Runnable {
		private float setupDistance;
		private float currentDistance;
		private boolean isSetup = false;
		private boolean isAlarmRunning = false;
		@Override
		public void run() {
			try(BufferedReader reader = new BufferedReader(new FileReader(new File("distance_file.txt")))) {
				//123.4
				//456.9
				if(!isSetup) {
					setupDistance = Float.parse(reader.readLine());
					isSetup = true;
				} else {
					currentDistance = Float.parse(reader.readLine());
				}

				if(currentDistance <= setupDistance * .5F && !isAlarmRunning) {
	1				isAlarmRunning = true;
					initAlarm();
				}
				
				Thread.sleep(2000L);
			} catch(IOException e) { }
		}

		private void initAlarm() {
			//TODO: Have a different thread run the countdown
		}
	}


}
