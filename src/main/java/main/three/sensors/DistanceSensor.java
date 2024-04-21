package main.three.sensors;

import org.eclipse.californium.core.CoapResource;

public class DistanceSensor extends CoapResource {
	
	private float currentDistance;
	private float countDownTimer;

	public DistanceSensor() {
		super("distance");
		getAttributes().setTitle("Gets the Distance Sensors value");
		 currentDistance = -1F;
		 countDownTimer = -2;
	}
	
	public void triggerCountdown() {
		//TODO: Need to create loop for 30s.
		//TODO: If cancel action is given, then stop timer+
		countDownTimer = 30F;
	}
	
}
