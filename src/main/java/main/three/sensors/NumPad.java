package main.three.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for handling the NumPad interactions
 * @author Josh Moore
 *
 */
public class NumPad {
	
	public NumPad() {
		String line = null;
		String code = "123A";
		String checker = "";
		try {
			Process p = Runtime.getRuntime().exec("python numpad.py");
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			while((line = stdInput.readLine()) != null) {
				System.out.println(line);
				checker += line;
			}
			
			if(checker.equals(code))
				System.out.println("You have entered the correct code.");
			else
				System.out.println("Incorrect code.");
			
		} catch(IOException e) {
		}
	}
}
