package main.three.sensors;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;

import main.three.AlarmSystem;

/**
 * Helper class for the LED light. This is only used in tangent with the Buzzer. As such,
 * this only contains a minimal amount of code for handling the LED. This simply acts as
 * a medium for both the buzzer and LED (This is because both will be going off at the same time).
 * @author Josh Moore
 *
 */
public class NoiseController {
	private static final int LED_PIN = 2	;
	private DigitalOutput led;
	private Run run;
	private boolean isStarting;
	
	private AlarmSystem alarm;
	
	public NoiseController(Context context) {
//		DigitalOutputConfigBuilder ledConfig = DigitalOutput.newConfigBuilder(context).id("led").address(LED_PIN).shutdown(DigitalState.LOW).initial(DigitalState.LOW).provider("pigpio-digital-output");
		DigitalOutputConfigBuilder ledConfig = DigitalOutput.newConfigBuilder(context).id("led").name("LED Light").address(LED_PIN).shutdown(DigitalState.HIGH).initial(DigitalState.HIGH).provider("pigpio-digital-output");
		
//		led = context.create(ledConfig);
		led = context.create(ledConfig);
		isStarting = false;
		run = new Run();
	}
	
	public void turnLightOn() { led.low(); }
	public void turnLightOff() { led.high(); }
	public boolean isLightOn() { return led.isLow(); }
	
	public void start(AlarmSystem alarm) { run.run(); this.alarm = alarm; }
	public void end() { run.cancel(); }
	
	public boolean isAlarmOn() { return run.isAlarmOn(); }
	public boolean isAlarmStarting() { return isStarting; }
	
	private class Run implements Runnable {
		private boolean hitEnd = false;
		private int i;
		
		@Override
		public void run() {
			try {
				for(i = 0; i < 10; i++) {
					Thread.sleep(1000L);
					i++;
					
					if(i == 9)
						hitEnd = true;
				}
				
				while(hitEnd) {
					Thread.sleep(500L);
					
					if(isLightOn())
						turnLightOff();
					else
						turnLightOn();
					new NumPad(alarm);
					Thread.sleep(30000L);
				}
				
			} catch (InterruptedException e) { }
		}
		
		public boolean isAlarmOn() { return hitEnd; }
		
		public void cancel() { i = 31; hitEnd = false; }
	}
}
