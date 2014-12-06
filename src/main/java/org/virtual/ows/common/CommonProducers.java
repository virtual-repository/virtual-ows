package org.virtual.ows.common;

import static com.fasterxml.jackson.databind.SerializationFeature.*;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.Module;
import dagger.Provides;

@Module(library=true)
public class CommonProducers {

	

	@Provides
	@Singleton
	ObjectMapper mapper() {
		
		ObjectMapper mapper= new ObjectMapper();
		
		mapper.enable(INDENT_OUTPUT);
		
		return mapper;
	}
	

	
	
}
