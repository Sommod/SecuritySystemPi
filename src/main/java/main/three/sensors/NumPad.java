package main.three.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.io.gpio.digital.PullResistance;

/**
 * Class for handling the NumPad interactions
 * @author Josh Moore
 *
 */
public class NumPad {
//	/**
//	 *    12 16 20 21
//	 * 25 -  -  -  - 
//	 * 8  -  -  -  -
//	 * 7  -  -  -  -
//	 * 1  -  -  -  -
//	 */
//											//Row			//Col
//	private static final int[][] PIN_IDS = {{25, 8, 7, 1}, {12, 16, 20, 21}};
//	private Pins pins;
//	
//	public NumPad(Context context) {
//		pins = new Pins();
//		
//		for(int i = 0; i < 2; i++) {
//			for(int j = 0; j < 4; j++) {
//				pins.addInput(PIN_IDS[i][j], (i * 4) + j, context);
//				pins.getPin((i * 4) + j).addListener(new Temp((i * 4) + j));
//			}
//		}
//		
//		
//	}
//	
//	public void sendMessage(int slot) {
//		System.out.println("Slot hit: " + slot);
//	}
//	
//	private class Pins {
//		private DigitalInput[] inputs;
//		
//		public Pins() {
//			inputs = new DigitalInput[8];
//		}
//		
//		public void addInput(final int ID, int slot, Context context) {
//			DigitalInputConfigBuilder inputBuilder = DigitalInput.newConfigBuilder(context).id(slot + "-num").address(ID)
//					.pull(PullResistance.PULL_DOWN).debounce(3000L).provider("pigpio-digital-input");
//			inputs[slot] = context.create(inputBuilder);
//		}
//		
//		public DigitalInput getPin(int slot) {
//			return inputs[slot];
//		}
//		
//		public DigitalInput[] getInputs() { return inputs; }
//	}
//	
//	private class Temp implements DigitalStateChangeListener {
//		
//		private int id;
//		
//		public Temp(int id) { this.id = id; }
//
//		@Override
//		public void onDigitalStateChange(DigitalStateChangeEvent event) {
//			System.out.println("Event Occurred.");
//		}
//	}
	
	
	public NumPad() {
		String s = null;
		try {
			Process p = Runtime.getRuntime().exec("python numpad.py");
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			System.out.println("Here is the stadnard output of the command:\n");
			while((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}
			
			System.out.println("Here is the stadnard error of the command (if any):\n");
			while((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
			
			System.exit(0);
		} catch(IOException e) {
		}
	}
}
