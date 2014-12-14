package org.virtual.wfs.configuration;

import static java.util.stream.Collectors.*;
import static org.virtual.ows.common.Constants.*;
import static org.virtual.ows.common.Utils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.inject.Singleton;
import javax.xml.namespace.QName;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.ows.OwsProxy;
import org.virtual.ows.WfsClient;
import org.virtual.ows.common.CommonProducers;
import org.virtualrepository.RepositoryService;

import dagger.Module;
import dagger.Provides;

@Slf4j
@Module(includes=CommonProducers.class, library=true)
public class ConfigurationProducers {

	@Provides
	@Singleton
	List<WfsClient> clients(@NonNull Configuration configuration) {
		
		return configuration.services().stream().map(WfsClient::new).collect(toList());
	}
	
	@Provides
	@Singleton
	List<RepositoryService> services(List<WfsClient> clients) {
		
		return clients.stream().map($-> {
			
			QName name = $.service().name();
			String uri = $.service().uri();
		
			log.info("plugging geoserver {} @ {}",name,uri);
			
			OwsProxy proxy = new OwsProxy($);
			
			RepositoryService service = new RepositoryService(name,proxy);
			
			//service properties provided by the profile
			service.properties(proxy.profile());
			
			return service;

		})
		.collect(toList());
	}
	
	@Provides
	@Singleton
	Configuration configuration(ConfigurationLocator locator, ConfigurationContext ctx) {
		
		Logger log = LoggerFactory.getLogger(ConfigurationLocator.class);
				
		File location = locator.locate();
		
		String path = location.getAbsolutePath();
		
		InputStream stream = null;
		
		try {
		
			if (isValid(location)) {
				
				log.info("loading configuration @ {}",path);
					
				stream = new FileInputStream(location);
					
			}
			else {
				
				stream = ConfigurationLocator.class.getResourceAsStream("/"+config_filename);

				if (stream == null)
					throw new AssertionError("no configuration found on file system or classpath");

				log.info("starting with classpath configuration, persisting @ {}",path);
				
				
				
			}
			
			return ctx.bind(stream);
		}
		
		catch(Exception e) {
			throw new RuntimeException("cannot read the configuration @ " + path, e);
		}
	}

	
}
