package org.virtual.ows.profile;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.geotoolkit.wfs.xml.v100.FeatureTypeType;
import org.geotoolkit.wfs.xml.v100.WFSCapabilitiesType;
import org.virtual.ows.WfsClient;
import org.virtualrepository.Properties;
import org.virtualrepository.Property;


@Slf4j
public class Wfs100Profile implements WfsProfile {

	final WfsClient client;
	
	
	WFSCapabilitiesType capabilities;
	
	
	public Wfs100Profile(@NonNull WfsClient client) {
		
		this.client=client;
		
	}
	
	@Override
	public Properties properties() {
		
		Properties properties = new Properties();
		
		if (capabilities!=null)
			
			properties.add(
				
				new Property("title", capabilities.getServiceIdentification().getFirstTitle()),
				new Property("abstract", capabilities.getServiceIdentification().getFirstAbstract()),
				new Property("vendor", capabilities.getCapability().getVendorSpecificCapabilities()),
				new Property("provider",capabilities.getServiceProvider().getProviderName())	
					
			);
		
		return properties;
	}


	@Override
	public List<WfsTypeProfile> types() {
		
		List<WfsTypeProfile> profiles = new ArrayList<>();

		if (capabilities!=null)
			for (FeatureTypeType type : capabilities.getFeatureTypeList().getFeatureType())
				profiles.add(new Wfs100TypeProfile(type));
		
		return profiles;
	}
	
	
	public void refresh() {

		log.info("refreshing profile for {}", client.service().name());
			
		capabilities = client.capabilities().get(WFSCapabilitiesType.class);

		
	}
}
