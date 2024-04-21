package main.three;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;

public class LightController {
	private static final int LED_PIN = 0;
	private DigitalOutput led;
	
	public LightController(Context context) {
		DigitalOutputConfigBuilder ledConfig = DigitalOutput.newConfigBuilder(context).id("led").address(LED_PIN).shutdown(DigitalState.LOW).initial(DigitalState.LOW).provider("pigpio-digital-output");
		led = context.create(ledConfig);
	}
	
	public void turnLightOn() { led.low(); }
	public void turnLightOff() { led.high(); }
	public boolean isLightOn() { return led.isLow(); }
}