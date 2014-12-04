package org.acme;

import static javax.ws.rs.core.HttpHeaders.*;
import static javax.ws.rs.core.MediaType.*;
import static javax.ws.rs.core.Response.Status.Family.*;
import static org.junit.Assert.*;
import static org.virtual.geoserver.configuration.GeoServer.*;

import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.xml.namespace.QName;

import org.junit.Test;
import org.virtual.geoserver.GeoClient;
import org.virtual.geoserver.configuration.GeoServer;

public class IntegrationTests {

	@Test
	public void ping() {
		
		String uri = "http://www.fao.org/figis/geoserver/area/ows?service=WFS";
		GeoServer server = server(QName.valueOf("test"), uri);
		
		GeoClient client = new GeoClient(server);
		
		Response response = client.request().head();
		
		assertEquals(SUCCESSFUL,typeof(response));
		
	}
	
	
	
	
	///////////////////////////////////////////
	
	<T> Entity<T> bodyWith(T o) {
		return Entity.entity(o, APPLICATION_JSON);
	}

	
	Family typeof(Response response) {
		return response.getStatusInfo().getFamily();
	}
	
	String mediatypeof(Response response) {
		return response.getHeaderString(CONTENT_TYPE);
	}

	String someName() {
		return UUID.randomUUID().toString();
	}
}
