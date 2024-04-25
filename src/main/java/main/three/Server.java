package main.three;

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

/**
 * Server class that handles send/receive CoAP messages
 * from the Raspberry Pi to the requested Client.
 * 
 * @author Josh Moore
 */
public class Server extends CoapServer {
	
	static {
		CoapConfig.register();
	}
	
	private static Server instance;
	private static final int COAP_PORT = Configuration.getStandard().get(CoapConfig.COAP_PORT);
	
	public Server() throws SocketException {
		super();
		addEndpoints();
		add(new SmokeClass());
		add(new DistanceClass());
		
		instance = this;
		instance.start();
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
	
	private void closeServer() {
		instance.destroy();
		System.exit(0);
	}
	
	private class SmokeClass extends CoapResource {
		public SmokeClass() {
			super("smoke");
			getAttributes().setTitle("Handler for Smoke Detector.");
		}
		
		@Override
		public void handleGET(CoapExchange exchange) {
			exchange.respond("Smoke has been Detected! Alerting the Fire Department!");
			System.out.println("Smoke Has Been Detected! Alerting The Fire Department!");
		}
	}
	
	private class DistanceClass extends CoapResource {
		public DistanceClass() {
			super("distance");
			getAttributes().setTitle("This is the handler for the Distance Sensor.");
		}
		
		@Override
		public void handleGET(CoapExchange exchange) {
			System.out.println("Notice: An alarm has been tripped, 10 seconds before emergency services are called.");
		}
		
		@Override
		public void handlePOST(CoapExchange exchange) {
			System.out.println("Notice: Alarm has been diabled.");
		}
	}
	
}
