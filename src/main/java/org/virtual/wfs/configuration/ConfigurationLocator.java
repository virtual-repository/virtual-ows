package org.virtual.wfs.configuration;

import static org.virtual.ows.common.Constants.*;
import static org.virtual.ows.common.Utils.*;

import java.io.File;

import javax.inject.Inject;

import org.virtual.ows.common.Utils;


public class ConfigurationLocator {

	@Inject
	public ConfigurationLocator() {}

	public File locate() throws IllegalStateException {

		File dir = locate_directory();
		
		if (dir==null) {
			
			dir = new File(System.getProperty("user.dir"));
			
			if (!isValid(new File(dir,config_filename)))
				dir = new File(System.getProperty("user.home"));
		}
		
		
		return new File(dir,config_filename);

	}
	
	
	private File locate_directory() {
		
		String location = System.getProperty(config_property);
		
		if (location!=null) 
			return validDirectory(location);
		
		location = System.getenv(config_property);
		
		if (location!=null) 
			return validDirectory(location);
		
		return null;
	}
	
	private File validDirectory(String location) {
		
		File file = new File(location);
		try {
			Utils.validDirectory(file);
		}
		catch(Exception e) {
			throw unchecked("invalid configuration @ "+file,e);
		}
		
		return file;
				
	}

}
