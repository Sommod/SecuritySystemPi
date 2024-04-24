package main.three;

import main.three.sensors.NoiseController;
import main.three.sensors.NumPad;

public class AlarmSystem {
	private boolean isExitCode;
	private NoiseController controller;
	
	public AlarmSystem(NoiseController controller) {
		isExitCode = false;
		this.controller = controller;
	}
	
	public void start() {
		controller.start();
		new NumPad(this);
	}
	
	public void end() {
		controller.end();
	}
	
	public boolean isRunning() { return controller.isAlarmOn(); }
	public boolean isStarting() { return controller.isAlarmStarting(); }
	
	public void setExitCode() { isExitCode = true; }
	public boolean isExit() { return isExitCode; }

}
