package org.virtual.ows;

import static javax.ws.rs.client.ClientBuilder.*;

import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.glassfish.jersey.filter.LoggingFilter;


@RequiredArgsConstructor
public class OwsClient {

	@NonNull
	final OwsService service;
	
	public void ping() {
		client().target(service.uri()).request().head();
	}
	
	public Builder capabilities() {
		return make("GetCapabilities").request();
	}
	
	
	
	
	////////////////////////////////////////////////////////////////////////////// helpers
	
	
	private  WebTarget make(String $) {
		
		return client().target(service.uri())
				.queryParam("service","wfs")
				.queryParam("version",service.version())
				.queryParam("request", $);
	}
	
	private Client client() { 
		
		return newClient()
			.register(new LoggingFilter(Logger.getLogger(LoggingFilter.class.getName()),true));
	}
	
	
}
