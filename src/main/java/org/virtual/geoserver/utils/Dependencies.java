package org.virtual.geoserver.utils;

import javax.inject.Singleton;

import org.virtual.geoserver.Configuration;
import org.virtual.geoserver.GeoPlugin;

import dagger.Module;
import dagger.Provides;

@Module(injects = GeoPlugin.class)
public class Dependencies {

	

	@Provides
	@Singleton
	public Configuration configuration() {
		return new Configuration();
	}
}
