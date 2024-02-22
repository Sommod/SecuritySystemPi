package main;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.io.gpio.digital.impl.DefaultDigitalInputConfigBuilder;
import com.pi4j.io.gpio.digital.impl.DefaultDigitalOutputConfigBuilder;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;

/**
 * Main class of the Project
 * @author Josh Moore
 * @author Ben Kanter
 *
 */
public class Main {
	
	private static int pressCount = 0;
	private static final int PIN_BUTTON = 24; // PIN 18 = BCM 24
	private static final int PIN_LED = 22; // Pin 15 = BCM 22
	
	public static void main(String[] args) {
		
		// Main Class for Entire Pi4J usage. This must be the first object created.
		Context context = Pi4J.newAutoContext();
		
		// Used for printing to the console.
		Platforms platforms = context.platforms();
		Console console = new Console();
		
		console.box("Pi4J PLATFORMS");
		console.println();
		platforms.describe().print(System.out);
		console.println();
		
		
		// Create an InputConfig for button handling.
		DigitalInputConfigBuilder ddicb = DefaultDigitalInputConfigBuilder.newInstance(context);
		ddicb.id("button").name("Press Button").address(PIN_BUTTON).pull(PullResistance.PULL_DOWN).debounce(3000L).provider("pigpio-digital-input");
		
		// Create Button, Add Event Handler for button
		DigitalInput button =  context.create(ddicb);
		button.addListener(e -> {
			if(e.state() == DigitalState.LOW) {
				pressCount++;
				console.println("Button was pressed for the " + pressCount + "th time");
			}
		});
		
		// Create LED configure
		DigitalOutputConfigBuilder docb = DefaultDigitalOutputConfigBuilder.newInstance(context);
		docb.id("led").name("LED Flasher").address(PIN_LED).shutdown(DigitalState.LOW).initial(DigitalState.LOW).provider("pigpio-digital-output");
		
		// Create LED Object
		DigitalOutput led = context.create(docb);
		
		// Loop for LED based on button press
		while(pressCount < 5) {
			if(led.equals(DigitalState.HIGH))
				led.low();
			else
				led.high();
			try {
				Thread.sleep(500 / (pressCount + 1));
			} catch(InterruptedException e1) {
				System.err.println("Error, Could not use Thread, need fix...");
				e1.printStackTrace();
			}
		}
		
		// Method to shutdown Pi4J threads. Must call at end.
		context.shutdown();
	}
}
