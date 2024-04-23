package main.three.sensors;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;

public class DistanceSensor {
	
	private final int PINS[] = {4, 17}; //Trigger, Echo
	private DigitalInput input;
	private DigitalOutput output;
	
	public DistanceSensor(Context context) {
		
	}
	
}
