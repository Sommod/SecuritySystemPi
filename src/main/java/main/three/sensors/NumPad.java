package main.three.sensors;

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
	/**
	 *    12 16 20 21
	 * 25 -  -  -  - 
	 * 8  -  -  -  -
	 * 7  -  -  -  -
	 * 1  -  -  -  -
	 */
											//Row			//Col
	private static final int[][] PIN_IDS = {{25, 8, 7, 1}, {12, 16, 20, 21}};
	private Pins pins;
	
	public NumPad(Context context) {
		pins = new Pins();
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 4; j++) {
				pins.addInput(PIN_IDS[i][j], (i * 4) + j, context);
				pins.getPin((i * 4) + j).addListener(new Temp((i * 4) + j));
			}
		}
		
		
	}
	
	public void sendMessage(int slot) {
		System.out.println("Slot hit: " + slot);
	}
	
	private class Pins {
		private DigitalInput[] inputs;
		
		public Pins() {
			inputs = new DigitalInput[8];
		}
		
		public void addInput(final int ID, int slot, Context context) {
			DigitalInputConfigBuilder inputBuilder = DigitalInput.newConfigBuilder(context).id(slot + "-num").address(ID)
					.pull(PullResistance.PULL_DOWN).debounce(3000L).provider("pigpio-digital-input");
			inputs[slot] = context.create(inputBuilder);
		}
		
		public DigitalInput getPin(int slot) {
			return inputs[slot];
		}
		
		public DigitalInput[] getInputs() { return inputs; }
	}
	
	private class Temp implements DigitalStateChangeListener {
		
		private int id;
		private boolean triggered;
		
		public Temp(int id) { this.id = id; }

		@Override
		public void onDigitalStateChange(DigitalStateChangeEvent event) {
			triggered = true;
			sendMessage(id);
		}
	}
}
