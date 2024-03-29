package main.two;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.elements.config.Configuration;
import org.eclipse.californium.elements.util.NetworkInterfacesUtil;

public class CoAP_Server extends CoapServer {
	
	private final String W1_DEVICES_PATH = "/sys/bus/w1/devices/";
	private final String W1_DEVICE = "28-0000000782bd";
	private final String W1_SLAVE = "/w1_slave";
	private File slaveFile = new File(W1_DEVICES_PATH + W1_DEVICE + W1_SLAVE);
	
	static {
		CoapConfig.register();
	}
	private static final int COAP_PORT = 
			Configuration
			.getStandard()
			.get(CoapConfig.COAP_PORT);
	private static final String tempUnit = "F";
	float temperature = 70;

	// Temp Remove
	
	public static void main(String[] args) {
		try {
			CoAP_Server server = new CoAP_Server();
			server.start();
		} catch(Exception e) {
			System.err.println("CoAP server err: " + e.getMessage());
		}
	}

	public CoAP_Server() throws SocketException {
		super();
		addEndpoints();
		add(new TemperatureResource());
	}

	private void addEndpoints() {
		Configuration config = Configuration.getStandard();
		// Add an endpoint listener for each host network interface
		for(InetAddress addr : NetworkInterfacesUtil.getNetworkInterfaces()) {
			InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
			CoapEndpoint.Builder builder = new CoapEndpoint.Builder();
			builder.setInetSocketAddress(bindToAddress);
			builder.setConfiguration(config);
			addEndpoint(builder.build());
		}
	}

	class TemperatureResource extends CoapResource {

		public TemperatureResource() {
			super("temp");
			getAttributes().setTitle("Server room temperature");
		}
		// public HelloWorldResource() {
		// super("temp"); // set resource URI identifier
		// getAttributes().setTitle("Server room temperature");
		// }

		@Override
		public void handleGET(CoapExchange exchange) {
			// get latest temperature reading and return it
			temperature = getTemperature(); 
			exchange.respond(temperature + " degrees " + tempUnit);
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
