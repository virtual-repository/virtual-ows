package org.virtual.ows.profile;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.geotoolkit.wfs.xml.v100.FeatureTypeType;
import org.geotoolkit.wfs.xml.v100.WFSCapabilitiesType;
import org.virtual.ows.WfsClient;
import org.virtualrepository.Properties;
import org.virtualrepository.Property;


public class Wfs100Profile extends WfsBaseProfile {

	WFSCapabilitiesType capabilities;
	
	public Wfs100Profile(WfsClient client) {
		super(client);		
	}
	
	@Override
	public void $update(Properties properties) {
		
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
		
	}


	@Override
	public List<WfsTypeProfile> types() {
		
		List<WfsTypeProfile> profiles = new ArrayList<>();

		if (capabilities!=null)
			for (FeatureTypeType type : capabilities.getFeatureTypeList().getFeatureType())
				profiles.add(new Wfs100TypeProfile(type));
		
		return profiles;
	}
	
	
	public void $refresh() {

		capabilities = client.capabilities().get(WFSCapabilitiesType.class);
		
	}
}
