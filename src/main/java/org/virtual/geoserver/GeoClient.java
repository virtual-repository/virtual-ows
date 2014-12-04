package org.virtual.geoserver;

import static javax.ws.rs.client.ClientBuilder.*;

import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.glassfish.jersey.filter.LoggingFilter;
import org.virtual.geoserver.configuration.GeoServer;


@RequiredArgsConstructor
public class GeoClient {

	@NonNull
	final GeoServer configuration;
	
	
	public Builder request() {
		return at("");
	}
	
	public Builder at(String path) {
		return client().target(address(path)).request();
	}
	
	
	////////////////////////////////////////////////////////////////////////////// helpers
	
	private Client client() { 
		
		return newClient()
			.register(new LoggingFilter(Logger.getLogger(LoggingFilter.class.getName()),true));
	}
	
	private String address(String path) {
		return configuration.uri()+(clean(path));
	}
	
	private String clean(String path) {
		return path.isEmpty()?path: path.startsWith("/")? path:"/"+path;
	}
	
}
