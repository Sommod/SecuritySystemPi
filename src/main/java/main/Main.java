package main;

import com.pi4j.Pi4J;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;

/**
 * Main class of the Project
 * @author Josh Moore
 * @author Ben Kanter
 *
 */
public class Main {
	
	private static int pressCount = 1;
	private static int exitCount = 0;
	private static boolean LED_CHOICE = true;
	private static boolean stopBlinkChange = false;
	private static final int PIN_BUTTON_2 = 23; // I think this is the pin #
	private static final int PIN_BUTTON_1 = 24; // PIN 18 = BCM 24
	private static final int PIN_LED_1 = 22; // Pin 15 = BCM 22
	private static final int PIN_LED_2 = 10; // Pin 19 = BCM 10
	//private static final int TEMP_SENSOR = 20; // PIN 38 = BCM 20
	
	public static void main(String[] args) {
		
		// Main Class for Entire Pi4J usage. This must be the first object created.
		var context = Pi4J.newAutoContext();
		
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
					console.println("Auto-Change Flasher is currently: " + (stopBlinkChange ? "Paused" : "Auto-Changing"));
					console.println("-- Exit Count is: " + exitCount + " / 5");
					exitCount++;
				} else { // If TRUE
					pressCount++;
					
					if(pressCount >= 6)
						pressCount = 1;
					
					console.println("Current Dim setting: " + pressCount + " / 5");
				}
			}
		});
		
		buttonConfig = DigitalInput.newConfigBuilder(context).id("button_2").name("Dimmer Buttom").address(PIN_BUTTON_2).pull(PullResistance.PULL_DOWN).debounce(3000L).provider("pigpio-digital-input");
		var button_2 = context.create(buttonConfig);
		
		button_2.addListener(e2 -> {
			if(e2.state() == DigitalState.LOW) {
				LED_CHOICE = !LED_CHOICE;
				console.println("Current Config of Button (1) is set to: " + (LED_CHOICE ? "Dimmer" : "Auto-Change Flasher"));
			}
		});

		var ledConfig = DigitalOutput.newConfigBuilder(context)
		      .id("led")
		      .name("LED Flasher")
		      .address(PIN_LED_1)
		      .shutdown(DigitalState.LOW)
		      .initial(DigitalState.LOW)
		      .provider("pigpio-digital-output");
		      
		var led = context.create(ledConfig);
		
		ledConfig = DigitalOutput.newConfigBuilder(context)
				.id("led_1")
				.name("LED Dimmer")
				.address(PIN_LED_2)
				.shutdown(DigitalState.LOW)
				.initial(DigitalState.LOW)
				.provider("pigpio-digital-output");
		var led_2 = context.create(ledConfig);
		
		short mode_1 = 1;

		while (exitCount < 5) {
		      if (led.equals(DigitalState.HIGH)) {
		           led.low();
		           led_2.low();
		      } else {
		           led.high();
		           led_2.high();
		      }
		      try {
				Thread.sleep(500 / (mode_1 <= 4 ? 1 : mode_1 <= 8 ? 2 : 5));
				
				if(!stopBlinkChange)
					mode_1++;
				
				if(mode_1 >= 20)
					mode_1 = 1;
			} catch(InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		context.shutdown();
	}
}
