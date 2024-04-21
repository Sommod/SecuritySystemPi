package main.three.sensors;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.io.gpio.digital.PullResistance;

public class SmokerDetectorSensor {
	private DigitalInput smoker;
	private final int SMOKE_PIN = 27;
	
	public SmokerDetectorSensor(Context context) {
		DigitalInputConfigBuilder smokeBuilder = DigitalInput.newConfigBuilder(context).id("smoker").address(SMOKE_PIN)
				.pull(PullResistance.PULL_DOWN).debounce(3000L).provider("pigpio-digital-input");
		
		smoker = context.create(smokeBuilder);
		smoker.addListener(new Temp());
	}
	
	private void sendMessage() {
		System.out.println("digital state of smoker has changed.");
	}
	
	private class Temp implements DigitalStateChangeListener {

		@Override
		public void onDigitalStateChange(DigitalStateChangeEvent event) {
			sendMessage();
		}
		
	}
}
