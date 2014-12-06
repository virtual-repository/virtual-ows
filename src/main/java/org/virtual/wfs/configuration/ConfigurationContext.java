package org.virtual.wfs.configuration;

import static org.virtual.ows.common.Utils.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import lombok.Cleanup;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ConfigurationContext {

	@Inject
	ObjectMapper mapper;

	
	public Configuration bind(InputStream stream) {

		try {

			return mapper.readValue(stream,Configuration.class);

		}
		catch (Exception e) {
			throw unchecked("cannot read configuration (see cause)", e);
		}
	}
	
	public void save(Configuration cfg, File location) {

		try {
			
			@Cleanup FileOutputStream stream = new FileOutputStream(location);

			mapper.writeValue(stream, cfg);

		}
		catch (Exception e) {
			throw unchecked("cannot save configuration (see cause)", e);
		}
	}
	

}
