package org.acme;

import static java.util.Arrays.*;
import static javax.xml.namespace.QName.*;
import static org.junit.Assert.*;
import static org.virtual.ows.OwsService.*;
import static org.virtual.ows.common.Utils.*;
import static org.virtual.wfs.configuration.Configuration.*;

import org.junit.Test;
import org.virtual.wfs.configuration.Configuration;

public class ConfigurationTest {

	
	@Test
	public void configurationRoundTrips() {
		
		Configuration configuration = config().services(asList(
				service(valueOf("http://acme.org/somename"),"http://acme.org/someuri"),
				service(valueOf("http://acme.org/somename"),"http://acme.org/someuri")
		));     
		
		assertEquals(configuration,jsonRoundtrip(configuration));
	}
}
