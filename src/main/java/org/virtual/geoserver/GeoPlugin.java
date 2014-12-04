package org.virtual.geoserver;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.virtual.geoserver.common.CommonProducers;
import org.virtual.geoserver.configuration.ConfigurationProducers;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.spi.Plugin;

import dagger.Module;
import dagger.ObjectGraph;

//entrypoint, serves also as top-level dependency module

@Module(injects=GeoPlugin.class,includes={CommonProducers.class, ConfigurationProducers.class})
public class GeoPlugin implements Plugin {

	@Inject
	List<RepositoryService> proxies;

	@Override
	public Collection<RepositoryService> services() {
		
		//self-injects and bootstraps DI
		ObjectGraph.create(this).inject(this);

		return proxies;
	}
	
	
}
