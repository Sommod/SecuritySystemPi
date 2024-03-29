package main.two;

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
	private static final int COAP_PORT = 
            Configuration.getStandard().get( CoapConfig.COAP_PORT );
    private static final String tempUnti = "F";
    float temperature = 70;
    CoapTempServer

    public static void main(String[] args) {
        try {
            CoapTempServer server = new CoapTempServer();
            server.start();
        }
        catch ( Exception e ) {
            System.err.println("CoAP server err: " + e.getMessage());
        }
    }

    public CoapTempServer() throws SocketException {
        super();
        addEndpoints();
        add( new TemperatureResource() );
    }

    private void addEndpoints() {
        Configuration config = Configuration.getStandard();
        // Add an endpoint listener for each host network interface
        for (InetAddress addr : 
             NetworkInterfacesUtil.getNetworkInterfaces()) {
            InetSocketAddress bindToAddress = 
                new InetSocketAddress(addr, COAP_PORT);
            CoapEndpoint.Builder builder = new CoapEndpoint.Builder();
            builder.setInetSocketAddress(bindToAddress);
            builder.setConfiguration(config);
            addEndpoint( builder.build() );
        }
    }

    class TemperatureResource extends CoapResource {
        public HelloWorldResource() {
            super("temp"); // set resource URI identifier
            getAttributes().setTitle("Server room temperature");
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            // get latest temperature reading and return it
            temperature = …
            exchange.respond(temperature + " degrees " + tempUnit);
        }
    }
}
