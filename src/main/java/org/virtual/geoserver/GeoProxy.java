package org.virtual.geoserver;

import static java.util.Collections.*;

import java.util.List;

import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Lifecycle;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.spi.ServiceProxy;

public class GeoProxy implements ServiceProxy, Lifecycle {

	final GeoBrowser browser;
	final List<GeoReader> readers;
	
	public GeoProxy(GeoBrowser browser, List<GeoReader> readers) {
		this.browser=browser;
		this.readers=readers;
	}
	
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Browser browser() {
		return browser;
	}

	@Override
	public List<? extends Importer<?, ?>> importers() {
		return readers;
	}

	@Override
	public List<? extends Publisher<?, ?>> publishers() {
		return emptyList();
	}

}
