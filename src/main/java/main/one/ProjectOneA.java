package main.one;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;

/**
 * 
 * @author Josh Moore
 * @author Ben Kanter
 * 
 * This class is a simple wrapper for handling the first part of
 * the Project (1).
 *
 */
public class ProjectOneA {
	
	private int dimAmount = 25;
	private int exitCount = 0;
	private boolean LED_CHOICE = true;
	private boolean stopBlinkChange = false;
	private final int PIN_BUTTON_2 = 23; // PIN 16 = BCM 23
	private final int PIN_BUTTON_1 = 24; // PIN 18 = BCM 24
	private final int PIN_LED_1 = 22; // Pin 15 = BCM 22
	private final int PIN_LED_2 = 10; // Pin 19 = BCM 10
	
	private Context context;
	
	public ProjectOneA(Context context) {
		this.context = context;
		run();
	}
	
	private void run() {
		// Used for printing to the console.
		Platforms platforms = context.platforms();
		var console = new Console();
		console.box("Pi4J PLATFORMS");
		console.println();
		platforms.describe().print(System.out);
		console.println();
		
		var buttonConfig = DigitalInput.newConfigBuilder(context).id("button_1").name("Flasher Button").address(PIN_BUTTON_1).pull(PullResistance.PULL_DOWN).debounce(3000L).provider("pigpio-digital-input");
		var button = context.create(buttonConfig);
		
		button.addListener(e -> {
			if(e.state() == DigitalState.LOW) {
				if(!LED_CHOICE) { // If FALSE
					stopBlinkChange = !stopBlinkChange;
					console.println(
							"Auto-Change Flasher is currently: " + (stopBlinkChange ? "Paused" : "Auto-Changing"));
					console.println("-- Exit Count is: " + exitCount + " / 5");
					exitCount++;
				} else { // If TRUE
					if(dimAmount + 25 > 100)
						dimAmount = 25;
					else
						dimAmount += 25;
					console.println("Current Dim setting: " + dimAmount + "%");
				}
			}
		});
		
		buttonConfig = DigitalInput.newConfigBuilder(context).id("button_2").name("Dimmer Buttom").address(PIN_BUTTON_2).pull(PullResistance.PULL_DOWN).debounce(3000L).provider("pigpio-digital-input");
		var button_2 = context.create(buttonConfig);
		
		button_2.addListener(e2 -> {
			if(e2.state() == DigitalState.LOW) {
				LED_CHOICE = !LED_CHOICE;
				console.println(
						"Current Config of Button (1) is set to: " + (LED_CHOICE ? "Dimmer" : "Auto-Change Flasher"));
			}
		});
		
		var ledConfig = DigitalOutput.newConfigBuilder(context).id("led").name("LED Flasher").address(PIN_LED_1)
				.shutdown(DigitalState.LOW).initial(DigitalState.LOW).provider("pigpio-digital-output");
		var led = context.create(ledConfig);
		
		var pwmConfig = Pwm.newConfigBuilder(context).id("led_1-2").address(PIN_LED_2).initial(0).shutdown(0)
				.provider("pigpio-pwm");
		var pwmLed = context.create(pwmConfig);
		
		short mode_1 = 1;
		
		while(exitCount < 5) {
			if(led.equals(DigitalState.HIGH)) {
				led.low();
			} else {
				led.high();
			}
			
			pwmLed.on(dimAmount);
			
			try {
				Thread.sleep(500 / (mode_1 <= 4 ? 1 : mode_1 <= 8 ? 2 : 5));
				if(!stopBlinkChange)
					mode_1++;
				if(mode_1 >= 20)
					mode_1 = 1;
			} catch(InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
