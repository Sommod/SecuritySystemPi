package main.three.sensors;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.elements.exception.ConnectorException;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.io.gpio.digital.PullResistance;

public class SmokerDetectorSensor {
	private DigitalInput smoker;
	private final int SMOKE_PIN = 27;
	
	private URI uri;
	private CoapClient client;
	private	CoapResponse response;
	private Context context;
	
	private NoiseController noiseController;
	
	static {
		CoapConfig.register();
	}
	
	public SmokerDetectorSensor(Context context, NoiseController controller) {
		noiseController = controller;
		this.context = context;
		
		setupURI();
		createSmokeInput(context);
	}
	
	private void createSmokeInput(Context context) {
		DigitalInputConfigBuilder smokeBuilder = DigitalInput.newConfigBuilder(context).id("smoker").address(SMOKE_PIN)
				.pull(PullResistance.PULL_DOWN).debounce(3000L).provider("pigpio-digital-input");
		
		smoker = context.create(smokeBuilder);
		smoker.addListener(new Temp());
	}
	
	private void setupURI() {
		try {
			uri = new URI("coap://192.168.1.128/smoke");
			client = new CoapClient(uri);
			
		} catch(URISyntaxException e) { }
	}
	
	private void getResponse() {
		try {
			response = client.get();
			
			if(response != null) {
				@SuppressWarnings("unused")
				byte[] bytes = response.getPayload();
				System.out.println(response.getCode());
				System.out.println(response.getOptions());
				System.out.println(response.getResponseText());
				System.out.println("\nDETAILED RESPONSE:");
				System.out.println(Utils.prettyPrint(response));
				
				for(int i = 0; i < 3; i++) {
					if(noiseController.isLightOn())
						noiseController.turnLightOff();
					else
						noiseController.turnLightOn();
					
					Thread.sleep(500L);
				}
				
			} else {
				System.out.println("Response is NULL");
			}
		} catch (IOException | InterruptedException | ConnectorException e) { }
	}
	
	public void shutdown() {
		smoker.shutdown(context);
	}
	
	private class Temp implements DigitalStateChangeListener {

		@SuppressWarnings("rawtypes")
		@Override
		public void onDigitalStateChange(DigitalStateChangeEvent event) {
			if(event.state() == DigitalState.LOW)
				getResponse();
		}
		
	}
}
