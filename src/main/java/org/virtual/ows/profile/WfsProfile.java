package org.virtual.ows.profile;

import java.util.List;

import org.virtualrepository.Properties;

public interface WfsProfile {

	Properties properties();
	
	List<WfsTypeProfile> types();
	
	void refresh();
}
