package org.virtual.ows.profile;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.geotoolkit.wfs.xml.v200.FeatureTypeType;
import org.geotoolkit.wfs.xml.v200.WFSCapabilitiesType;
import org.virtual.ows.WfsClient;
import org.virtualrepository.Properties;
import org.virtualrepository.Property;


@Slf4j
public class Wfs200Profile implements WfsProfile {

	final WfsClient client;
	
	WFSCapabilitiesType capabilities;
	
	
	public Wfs200Profile(@NonNull WfsClient client) {
		
		this.client=client;
		
	}
	
	@Override
	public Properties properties() {
		
		Properties properties = new Properties();
		
		if (capabilities != null)
			properties.add(
					
				new Property("title",capabilities.getServiceIdentification().getFirstTitle()),
				new Property("abstract",capabilities.getServiceIdentification().getFirstAbstract()),
				new Property("keywords",capabilities.getServiceIdentification().getKeywords()
										.stream().flatMap($->$.getKeywordList().stream()).collect(toList()).toString()),
							
				new Property("type", capabilities.getServiceIdentification().getServiceType().getValue()),
				new Property("version",capabilities.getVersion()),
				new Property("provider",capabilities.getServiceProvider().getProviderName())
						
			);
		
		return properties;
	}


	@Override
	public List<WfsTypeProfile> types() {
		
		List<WfsTypeProfile> profiles = new ArrayList<>();

		if (capabilities!=null)
			for (FeatureTypeType type : capabilities.getFeatureTypeList().getFeatureType())
				profiles.add(new Wfs200TypeProfile(type));
		
		return profiles;
	}
	
	
	public void refresh() {

		log.info("refreshing profile for {}", client.service().name());
			
		capabilities = client.capabilities().get(WFSCapabilitiesType.class);

	}
}
