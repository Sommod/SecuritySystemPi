package main.three.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Handler class for the NumPad. Since Documention for the Java implementation of the NumPad
 * is rare and difficult to find, a Python file named 'numpad.py' is executed and used as a
 * driver for obtaining the Numpad input. This file simply obtains the Standard Input of the
 * file and uses it to determine if the code entered is a correct one.
 * @author Josh Moore
 *
 */
public class NumPad {
	
	private final String[] codes = {"123A","456B","789C"};
	private String input;
	private int codeEnter;
	
	public NumPad() {
		String line = null;
		input = "";
		codeEnter = -1;
		
		try {
			System.out.println("Enter in code:");
			Process p = Runtime.getRuntime().exec("python numpad.py");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			while((line = stdInput.readLine()) != null) {
				System.out.print(line);
				input += line;
			}
			
			while ((line = stdError.readLine()) != null) { }
			
			for(int i = 0; i < codes.length; i++) {
				if(codes[i].equals(input)) {
					codeEnter = i;
					break;
				}
			}
			
			System.exit(0);
		} catch(IOException e) {
		}
	}
	
	public int getCode() { return codeEnter; }
	public boolean isCorrectCode() { return codeEnter != -1; }
}
