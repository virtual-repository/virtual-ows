package org.virtual.ows.profile;

import java.util.ArrayList;
import java.util.List;

import org.geotoolkit.wfs.xml.v110.FeatureTypeType;
import org.geotoolkit.wfs.xml.v110.WFSCapabilitiesType;
import org.virtual.ows.WfsClient;
import org.virtualrepository.Properties;
import org.virtualrepository.Property;


public class Wfs110Profile extends WfsBaseProfile {

	WFSCapabilitiesType capabilities;
	
	
	public Wfs110Profile(WfsClient client) {
		super(client);	
	}
	
	
	@Override
	public void $update(Properties properties) {
		
		properties.add(
				
					new Property("title", capabilities.getServiceIdentification().getFirstTitle()),
					new Property("abstract", capabilities.getServiceIdentification().getFirstAbstract()),
					new Property("version", capabilities.getVersion()),
					new Property("provider",capabilities.getServiceProvider().getProviderName())
					
		);
	}


	@Override
	public List<WfsTypeProfile> types() {
		
		List<WfsTypeProfile> profiles = new ArrayList<>();

		if (capabilities!=null)
			for (FeatureTypeType type : capabilities.getFeatureTypeList().getFeatureType())
				profiles.add(new Wfs110TypeProfile(type));
		
		return profiles;
	}
	
	
	public void $refresh() {

		capabilities = client.capabilities().get(WFSCapabilitiesType.class);
		
	}
}
