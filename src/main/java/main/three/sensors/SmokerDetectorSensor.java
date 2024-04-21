package main.three.sensors;

import org.eclipse.californium.core.CoapResource;

public class SmokerDetectorSensor extends CoapResource {

	public SmokerDetectorSensor() {
		super("smoke");
	}
}
