package main.one;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.util.Console;

/**
 * 
 * @author Josh Moore
 * @author Ben Kanter
 *
 * This Class handles the Second step (part 2) of the project.
 */
public class ProjectOneB {
	//private final int TEMP_SENSOR = 4; // PIN 38 = BCM 4
	private final int THRESHOLD_VALUE = 78;
	
	private final int LED = 22; // Pin 15 = BCM 22
	
	private final String W1_DEVICES_PATH = "/sys/bus/w1/devices/";
	private final String W1_DEVICE = "28-0000000782bd";
	private final String W1_SLAVE = "/w1_slave";
	
	private File slaveFile;
	
	private Context context;
	private Console console;
	
	public ProjectOneB(Context context) {
		this.context = context;
		console = new Console();
		slaveFile = new File(W1_DEVICES_PATH + W1_DEVICE + W1_SLAVE);
		
		run();
	}
	
	private void run() {
		var ledConfig = DigitalOutput.newConfigBuilder(context).id("led").address(LED).shutdown(DigitalState.LOW).initial(DigitalState.LOW).provider("pigpio-digital-output");
		var led = context.create(ledConfig);
		
		led.low();
		float temp;
		
		// Infinite Loop
		while(true) {
			temp = getTemperature();
			
			if(temp >= THRESHOLD_VALUE)
				led.low();
			else
				led.high();
			
			try {
				Thread.sleep(3000L);
				console.println("Current Temperature from Sensor: " + temp);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private float getTemperature() {
		String temp = "";
		float ret = 0F;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(slaveFile))) {
			String line = "";
			
			while((line = reader.readLine()) != null) {
				if(line.contains("t="))  {
					temp = line;
					break;
				}
			}
			
			temp = temp.split("t=")[1];
			ret = ((Float.parseFloat(temp) / 1000) * 1.8F) + 32; // F Degrees
			
			reader.close();
		} catch (IOException | ArithmeticException e) {
			System.err.println("Could not read the File... Exiting Program");
			return -1;
		}
		
		return ret;
	}
}
