package main.three.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import main.three.AlarmSystem;

/**
 * Handler class for the NumPad. Since Documention for the Java implementation of the NumPad
 * is rare and difficult to find, a Python file named 'numpad.py' is executed and used as a
 * driver for obtaining the Numpad input. This file simply obtains the Standard Input of the
 * file and uses it to determine if the code entered is a correct one.
 * @author Josh Moore
 *
 */
public class NumPad {
	
	private final String[] codes = {"123A","456B"}; //Stop Alarm, Stop Program
	private String input;
	private int codeEnter;
	private AlarmSystem alarm;
	
	public NumPad(AlarmSystem alarm) {
		codeEnter = -1;
		this.alarm = alarm;
		
		new Temp().run();
	}
	
	private class Temp implements Runnable {
		@Override
		public void run() {
			while(codeEnter == -1) {
				String line = null;
				input = "";
				codeEnter = -1;
				
				try {
					System.out.println("Enter in code:");
					Process p = Runtime.getRuntime().exec("python numpad.py");
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					
					while((line = stdInput.readLine()) != null) {
						input += line;
					}
					
					while ((line = stdError.readLine()) != null) { }
					
					for(int i = 0; i < codes.length; i++) {
						if(codes[i].equals(input)) {
							codeEnter = i;
							break;
						}
					}
				} catch(IOException e) {
				}
			}
			
			if(codeEnter == 0)
				alarm.end();
			else if(codeEnter == 1)
				alarm.setExitCode();
		}
	}
	
	public int getCode() { return codeEnter; }
	public boolean isCorrectCode() { return codeEnter != -1; }
}
