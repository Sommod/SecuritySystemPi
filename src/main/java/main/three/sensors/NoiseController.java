package main.three.sensors;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;

/**
 * Helper class for the LED light. This is only used in tangent with the Buzzer. As such,
 * this only contains a minimal amount of code for handling the LED. This simply acts as
 * a medium for both the buzzer and LED (This is because both will be going off at the same time).
 * @author Josh Moore
 *
 */
public class NoiseController {
	private static final int LED_PIN = 2;
	private DigitalOutput led;
	
	public NoiseController(Context context) {
		DigitalOutputConfigBuilder ledConfig = DigitalOutput.newConfigBuilder(context).id("led").address(LED_PIN).shutdown(DigitalState.LOW).initial(DigitalState.LOW).provider("pigpio-digital-output");
		led = context.create(ledConfig);
	}
	
	public void turnLightOn() { led.low(); }
	public void turnLightOff() { led.high(); }
	public boolean isLightOn() { return led.isLow(); }
}
