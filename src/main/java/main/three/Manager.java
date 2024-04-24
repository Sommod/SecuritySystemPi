package main.three;

import com.pi4j.context.Context;

import main.three.sensors.DistanceSensor;
import main.three.sensors.NoiseController;
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
	
	private Context context;
	private AlarmSystem alarm;
	
	public Manager(Context context) {
		this.context = context;
		
		InitializeSensors();
		run();
	}
	
	private void InitializeSensors() {
		alarm = new AlarmSystem(noiseController);
		noiseController = new NoiseController(context);
		distanceSensor = new DistanceSensor(context, alarm);
		smokerDetectorSensor = new SmokerDetectorSensor(context, noiseController);
	}
	
	public void run() {
		while(!alarm.isExit()) {
			try { Thread.sleep(500L); }
			catch(InterruptedException e) {}
		}
		
		distanceSensor.shutdown();
		smokerDetectorSensor.shutdown();
	}
	
}
