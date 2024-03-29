package main.two;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.elements.exception.ConnectorException;

public class CoapGetClient {
	
	static {
		CoapConfig.register();
	}

	public static void main(String args[]) throws URISyntaxException {
		// make synchronous get call
		URI uri = new URI("coap://192.168.1.168:5683/temp");
		CoapClient client = new CoapClient(uri);
		CoapResponse response;
		try {
			response = client.get();
			if(response != null) {
				byte[] bytes = response.getPayload();
				System.out.println(response.getCode());
				System.out.println(response.getOptions());
				System.out.println(response.getResponseText());
				System.out.println("\nDETAILED RESPONSE:");
				System.out.println(Utils.prettyPrint(response));
			} else {
				System.out.println("Response is NULL");
			}
		} catch(ConnectorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
