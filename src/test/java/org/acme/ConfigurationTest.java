package org.acme;

import static java.util.Arrays.*;
import static javax.xml.namespace.QName.*;
import static org.junit.Assert.*;
import static org.virtual.geoserver.common.Utils.*;
import static org.virtual.geoserver.configuration.Configuration.*;
import static org.virtual.geoserver.configuration.GeoServer.*;

import org.junit.Test;
import org.virtual.geoserver.configuration.Configuration;

public class ConfigurationTest {

	
	@Test
	public void configurationRoundTrips() {
		
		Configuration configuration = config().servers(asList(
				server(valueOf("http://acme.org/somename"),"http://acme.org/someuri"),
				server(valueOf("http://acme.org/somename"),"http://acme.org/someuri")
		));     
		
		assertEquals(configuration,jsonRoundtrip(configuration));
	}
}
