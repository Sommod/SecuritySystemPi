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
		add(new AckClass());
		
		instance = this;
		instance.start();
	}
	
//	public static void main(String[] args) {
//		try {
//			instance = new Server();
//			instance.start();
//		} catch(Exception e) {
//			System.err.println("CoAP server err: " + e.getMessage());
//		}
//	}
	
//	public Server() throws SocketException {
//		super();
//		addEndpoints();
//	}
//	
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
	
	public void closeServer() { instance.destroy(); }
	
	public class AckClass extends CoapResource {
		public AckClass() {
			super("smoke");
			getAttributes().setTitle("Handler for Smoke Detector.");
		}
		
		@Override
		public void handleGET(CoapExchange exchange) {
			exchange.respond("Smoke has been Detected! Alerting the Fire Department!");
			System.out.println("Smoke Has Been Detected! Alerting The Fire Department!");
		}
	}
	
}
