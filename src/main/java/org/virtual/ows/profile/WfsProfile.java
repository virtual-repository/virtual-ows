package org.virtual.ows.profile;

import java.util.List;

import org.virtualrepository.impl.PropertyProvider;

public interface WfsProfile extends PropertyProvider {

	List<WfsTypeProfile> types();
	
	void refresh();
}
