package main.three.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.elements.exception.ConnectorException;

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
		
		handleGET();
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
			
			if(codeEnter == 0) {
				alarm.end();
				handlePOST();
			} else if(codeEnter == 1)
				alarm.setExitCode();
		}
	}
	
	private void handlePOST() {
		URI uri;
		try {
			uri = new URI("coap://192.168.1.128/distance");
			CoapClient client = new CoapClient(uri);
			byte[] b = new byte[10];
			
			CoapResponse response = client.post(b, 1);
			
			if(response != null) {
				@SuppressWarnings("unused")
				byte[] bytes = response.getPayload();
				System.out.println(response.getCode());
				System.out.println(response.getOptions());
				System.out.println(response.getResponseText());
				System.out.println("\nDETAILED RESPONSE:");
				System.out.println(Utils.prettyPrint(response));
				
			} else {
				System.out.println("Response is NULL");
			}
		} catch(URISyntaxException | ConnectorException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleGET() {
		URI uri;
		try {
			uri = new URI("coap://192.168.1.128/distance");
			CoapClient client = new CoapClient(uri);
			
			CoapResponse response = client.get();
			
			if(response != null) {
				@SuppressWarnings("unused")
				byte[] bytes = response.getPayload();
				System.out.println(response.getCode());
				System.out.println(response.getOptions());
				System.out.println(response.getResponseText());
				System.out.println("\nDETAILED RESPONSE:");
				System.out.println(Utils.prettyPrint(response));
				
			} else {
				System.out.println("Response is NULL");
			}
		} catch(URISyntaxException | ConnectorException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getCode() { return codeEnter; }
	public boolean isCorrectCode() { return codeEnter != -1; }
}
