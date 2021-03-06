package org.virtual.ows;

import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;

import org.virtual.ows.profile.Wfs100Profile;
import org.virtual.ows.profile.Wfs110Profile;
import org.virtual.ows.profile.Wfs200Profile;
import org.virtual.ows.profile.WfsProfile;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.spi.ServiceProxy;


@Getter
public class OwsProxy implements ServiceProxy {

	private final OwsBrowser browser;
	
	private final List<? extends Importer<?, ?>> importers;
	
	private WfsProfile profile;
	
	public OwsProxy(@NonNull WfsClient client) {
		
		profile = profileFor(client);
		
		browser=new OwsBrowser(profile,client);
		
		importers= asList(
				
						new WfsRawReader(client),
						new WfsReader(client)
	
						//may well grow with the plugin
					);
			
	}
	
	@Override
	public List<? extends Publisher<?, ?>> publishers() {
		return emptyList();
	}
	
	
	///////////////////////////////////////////////////////////
	
	public WfsProfile profileFor(WfsClient client) {
		
		switch (client.service().version()) {
			
			case v100 : 	return new Wfs100Profile(client); 	
							
			case v200 : 	return new Wfs200Profile(client);	
							
			default: 		return new Wfs110Profile(client);
		
		}
		
	}
}
