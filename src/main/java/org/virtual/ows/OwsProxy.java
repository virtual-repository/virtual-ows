package org.virtual.ows;

import static java.util.Collections.*;

import java.util.List;

import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.spi.ServiceProxy;

public class OwsProxy implements ServiceProxy {

	final OwsBrowser browser;
	final List<? extends Importer<?, ?>> importers;
	
	public OwsProxy(OwsBrowser browser, List<? extends Importer<?, ?>> importers) {
		this.browser=browser;
		this.importers=importers;
	}
	
	@Override
	public Browser browser() {
		return browser;
	}

	@Override
	public List<? extends Importer<?, ?>> importers() {
		return importers;
	}

	@Override
	public List<? extends Publisher<?, ?>> publishers() {
		return emptyList();
	}

}
