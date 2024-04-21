package main.three;

import com.pi4j.context.Context;

import main.three.sensors.DistanceSensor;
import main.three.sensors.NoiseController;
import main.three.sensors.NumPad;
import main.three.sensors.SmokerDetectorSensor;

/**
 * Main Handler class for the Final Project. This initializes all the GPIO ports on the breadboard.
 * The 'Alarm' system is initialized with the Buzzer/LED connected to the same GPIO port, the Distance
 * Sensor handling any change to initiate a count-down, and a Smoke detector for additional support.
 * 
 * @author Josh Moore
 *
 */
public class Manager {
	private DistanceSensor distanceSensor;
	private SmokerDetectorSensor smokerDetectorSensor;
	private NoiseController noiseController;
	private NumPad numPad;
	
	private Context context;
	private AlarmSystem alarm;
	
	private boolean soundAlarm;
	private boolean exitCode;
	
	public Manager(Context context) {
		this.context = context;
		soundAlarm = false;
		exitCode = false;
		
		InitializeSensors();
		run();
	}
	
	private void InitializeSensors() {
//		noiseController = new NoiseController(context);
//		distanceSensor = new DistanceSensor(context);
		smokerDetectorSensor = new SmokerDetectorSensor(context);
//		numPad = new NumPad(context);
		
//		alarm = new AlarmSystem();
		
		short time = 30;
		
		while(time > 0) {
			try { Thread.sleep(1000L); time--; }
			catch(InterruptedException e) { }
		}
	}
	
	public void run() {
		while(!exitCode) {
			if(!soundAlarm) { // Check Distance Sensor / Smoker Detector.
				//TODO: Get sensors hooked up (code-wise)
				//TODO: If Smoke Detector is going off, send message to server.
				//TODO: Else If distance is different (by a range)... then activate 'START' method in 'alarm'.
				//		Check if Alarm is already running, then ignore.
				//TODO: Else check if 'didSoundAlarm' is true, then set soundAlarm to true
			} else { // Running the alarm system.
				if(noiseController.isLightOn())
					noiseController.turnLightOff();
				else
					noiseController.turnLightOn();
			}
			
			try { Thread.sleep(500L); }
			catch(InterruptedException e) {}
		}
	}
	
}
