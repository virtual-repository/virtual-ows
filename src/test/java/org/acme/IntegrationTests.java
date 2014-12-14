package org.acme;

import static java.lang.System.*;
import static java.util.Collections.*;
import static javax.ws.rs.core.HttpHeaders.*;
import static javax.ws.rs.core.MediaType.*;
import static org.virtual.ows.OwsService.*;
import static org.virtual.ows.OwsService.Version.*;
import static org.virtualrepository.ows.WfsFeatureType.*;

import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.xml.namespace.QName;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.virtual.ows.OwsBrowser;
import org.virtual.ows.OwsProxy;
import org.virtual.ows.WfsClient;
import org.virtual.ows.WfsReader;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.ows.Features;
import org.virtualrepository.ows.WfsFeatureType;

@Slf4j
public class IntegrationTests {

	String endpoint =  "http://www.fao.org/figis/geoserver/ows";
	
	WfsClient client = new WfsClient(service(someName(),endpoint)
										.version(v110)
										.compress(true)
										.excludeGeom(true)
										.excludes(singleton("SUBOCEAN")));
	
	OwsProxy proxy = new OwsProxy(client);
	
	@Test
	public void ping() {
		
		client.ping();
		
	}
	
	@Test
	public void discover() {
		
		OwsBrowser browser = proxy.browser();
		
		System.out.println(browser.discover(emptyList()));
		
		
	}
	
	
	@Test
	public void retrieve() throws Exception {
		
		long time = currentTimeMillis();
		
		WfsReader reader = new WfsReader(client);
		
		WfsFeatureType asset = new WfsFeatureType("some", "fifao:FAO_MAJOR");
		
		Features features = reader.retrieve(asset);
	
		log.info("fetched {} features in {} ms.",features.all().size(),currentTimeMillis()-time);
		
	}
	
	@Test
	public void browseAndRetrieve() throws Exception {
		
		long time = currentTimeMillis();
		
		OwsBrowser browser = proxy.browser();
		
		WfsReader reader = new WfsReader(client);
		
		WfsFeatureType asset = (WfsFeatureType) browser.discover(emptyList()).iterator().next();
	
		Features features = reader.retrieve(asset);
	
		log.info("fetched {} features in {} ms.",features.all().size(),currentTimeMillis()-time);
		
	}
	
	@Test
	public void browseWithVR() throws Exception {
		
		VirtualRepository repo = new Repository();
		
		repo.discover(type);
		
		System.out.println(repo.services().iterator().next().properties());
		
	}
	
	@Test
	public void browseAndRetrieveWithVR() throws Exception {
		
		long time = currentTimeMillis();
		
		VirtualRepository repo = new Repository();
		
		repo.discover(type);
		
		Features features = repo.retrieve(repo.iterator().next(),Features.class);
		
		log.info("fetched {} features in {} ms.",features.all().size(),currentTimeMillis()-time);
		
		System.out.println(repo.services());
		
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
