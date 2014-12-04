package org.virtual.geoserver;

import static java.util.Arrays.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.virtual.geoserver.utils.Dependencies;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.spi.Plugin;

import dagger.Module;
import dagger.ObjectGraph;

@Module(includes=Dependencies.class)
public class GeoPlugin implements Plugin {

	@Inject
	Configuration configuration;

	@Override
	public Collection<RepositoryService> services() {
		
		ObjectGraph.create(this).inject(this);


		GeoserverBrowser browser = new GeoserverBrowser();
		List<GeoserverReader> readers = asList(new GeoserverReader());
		
		GeoserverProxy proxy = new GeoserverProxy(browser,readers);
		
		RepositoryService service = new RepositoryService(new QName("todo"), proxy);
		
		return Collections.singleton(service);
	}
	
	
}
