package org.acme;

import static java.util.Collections.*;
import static javax.ws.rs.core.HttpHeaders.*;
import static javax.ws.rs.core.MediaType.*;
import static org.virtual.ows.OwsService.*;

import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.xml.namespace.QName;

import net.opengis.wfs.v_1_1_0.WFSCapabilitiesType;

import org.junit.Test;
import org.virtual.ows.OwsBrowser;
import org.virtual.ows.OwsClient;

public class IntegrationTests {

	String endpoint =  "http://www.fao.org/figis/geoserver/ows";
	
	OwsClient client = new OwsClient(service(someName(),endpoint));
	
	@Test
	public void ping() {
		
		client.ping();
		
	}
	
	@Test
	public void fetchCapabilities() {
		
		client.capabilities().get(WFSCapabilitiesType.class);
		
	}
	
	@Test
	public void browse() {
		
		OwsBrowser browser = new OwsBrowser(client);
		
		System.out.println(browser.discover(emptyList()));
		
		client.capabilities().get(WFSCapabilitiesType.class);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////// every little helps

	
	<T> Entity<T> bodyWith(T o) {
		return Entity.entity(o, APPLICATION_JSON);
	}

	
	Family typeof(Response response) {
		return response.getStatusInfo().getFamily();
	}
	
	String mediatypeof(Response response) {
		return response.getHeaderString(CONTENT_TYPE);
	}

	QName someName() {
		return QName.valueOf(UUID.randomUUID().toString());
	}
}
