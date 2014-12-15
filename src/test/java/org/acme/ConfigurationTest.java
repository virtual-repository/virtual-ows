package org.acme;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.virtual.ows.OwsService.*;
import static org.virtual.ows.OwsService.Version.*;
import static org.virtual.ows.common.Utils.*;
import static org.virtual.wfs.configuration.Configuration.*;
import static org.virtual.wfs.configuration.Configuration.Mode.*;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.virtual.wfs.configuration.Configuration;

public class ConfigurationTest {

	
	@Test
	public void configurationRoundTrips() {
		
		Configuration configuration = config()
				.mode(development)
				.services(asList(
					service(QName.valueOf("http://acme.org/somename"),"http://acme.org/someuri"),
					service(QName.valueOf("http://acme.org/somename"),"http://acme.org/someuri")
								 .compress(false)
								 .excludeGeom(true)
								 .excludes(singleton("test"))
		));     
		
		assertEquals(configuration,jsonRoundtrip(configuration));
	}
	
	@Test
	public void versionsAreOrdered() {
		
		assertTrue(v100.before(v200));
	}
}
