package org.acme;

import static java.lang.System.*;
import static java.util.Collections.*;
import static javax.ws.rs.core.HttpHeaders.*;
import static javax.ws.rs.core.MediaType.*;
import static org.junit.Assert.*;
import static org.virtual.ows.OwsService.*;
import static org.virtual.ows.OwsService.Version.*;
import static org.virtual.wfs.configuration.Configuration.Mode.*;
import static org.virtualrepository.ows.WfsFeatureType.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.xml.namespace.QName;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
//github.com/virtual-repository/virtual-ows.git
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.virtual.ows.OwsBrowser;
import org.virtual.ows.OwsProxy;
import org.virtual.ows.WfsClient;
import org.virtual.ows.WfsReader;
import org.virtual.ows.profile.WfsProfile;
import org.virtualrepository.Properties;
import org.virtualrepository.Property;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.ows.Features;
import org.virtualrepository.ows.WfsFeatureType;

/**
 * Integration tests for the virtual-ows WFS client. Tests are parametrized upon
 * WFS version: (1.0, 1.1, 2.0). Version 2.0 doesn't work for the time being.
 * 
 *
 */
@Slf4j
@RunWith(Parameterized.class)
public class IntegrationTests {

	String endpoint =  "http://www.fao.org/figis/geoserver/fifao/ows";
	
	WfsClient client;
	
	OwsProxy proxy;
	
	@Parameters(name = "{index}: WFS {0}")
	public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
        		{v100}, {v110}
        });
    }
	
	@Parameter
	public Version wfsVersion;

	
	@Before
	public void setUp(){
		
		client = new WfsClient(service(someName(),endpoint)
										.version(wfsVersion)
										.compress(true)
										.excludeGeom(true)
										.excludes(singleton("SUBOCEAN"))).mode(development);
		
		proxy = new OwsProxy(client);		
	}
	
	@Test
	public void ping() {
		
		client.ping();
		
	}
	
	@Test
	public void fetchProfile() {
		
		WfsProfile profile = proxy.profile();
		
		profile.refresh();
		
		//test properties
		Properties properties = profile.properties();
		assertNotNull(properties.lookup("title").value());
		assertNotNull(properties.lookup("abstract").value());
		assertNotNull(properties.lookup("keywords").value());
		assertNotNull(properties.lookup("type").value());
		assertEquals(wfsVersion.value(), properties.lookup("version").value());
		if(wfsVersion != Version.v100){
			assertNotNull(properties.lookup("provider").value());
			assertEquals(6, properties.size());
		}else{
			assertEquals(5, properties.size());
		}	
			
		for(Property prop : properties)
			log.info("Property '{}' = {}",prop.name(),prop.value());
		
		assertFalse(profile.types().isEmpty());
		
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
		
		//enough to inspect property publication
		System.out.println(repo.services().iterator().next().properties());
		
	}
	
	@Test
	public void browseAndRetrieveWithVR() throws Exception {
		
		long time = currentTimeMillis();
		
		VirtualRepository repo = new Repository();
		
		repo.discover(type);
		
		Features features = repo.retrieve(repo.iterator().next(),Features.class);
		
		log.info("fetched {} features in {} ms.",features.all().size(),currentTimeMillis()-time);
		
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
