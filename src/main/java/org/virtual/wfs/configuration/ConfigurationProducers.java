package org.virtual.wfs.configuration;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.virtual.ows.common.Constants.*;
import static org.virtual.ows.common.Utils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.inject.Singleton;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.ows.OwsBrowser;
import org.virtual.ows.OwsClient;
import org.virtual.ows.OwsProxy;
import org.virtual.ows.WfsReader;
import org.virtual.ows.common.CommonProducers;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.spi.Importer;

import dagger.Module;
import dagger.Provides;

@Slf4j
@Module(includes=CommonProducers.class, library=true)
public class ConfigurationProducers {

	
	@Provides
	@Singleton
	List<RepositoryService> services(@NonNull Configuration configuration) {
		
		return configuration.services().stream().map($-> {
		
			log.info("plugging geoserver {} @ {}",$.name(),$.uri());
			
			OwsClient client  = new OwsClient($);
			
			List<? extends Importer<?,?>> importers =  asList(new WfsReader(client)); // start with one reader
			
			OwsProxy proxy = new OwsProxy(new OwsBrowser(client), importers);
			
			return new RepositoryService($.name(),proxy);
		
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
