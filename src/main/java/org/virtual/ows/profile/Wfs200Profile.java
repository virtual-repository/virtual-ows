package org.virtual.ows.profile;

import static java.util.stream.Collectors.toList;
import static org.virtual.ows.common.Utils.unchecked;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.xml.MarshallerPool;
import org.geotoolkit.wfs.xml.WFSMarshallerPool;
import org.geotoolkit.wfs.xml.v200.FeatureTypeType;
import org.geotoolkit.wfs.xml.v200.WFSCapabilitiesType;
import org.virtual.ows.WfsClient;
import org.virtualrepository.Properties;
import org.virtualrepository.Property;


public class Wfs200Profile extends WfsBaseProfile {

	WFSCapabilitiesType capabilities;
	
	public Wfs200Profile(WfsClient client) {
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
				profiles.add(new Wfs200TypeProfile(type));
		
		return profiles;
	}
	
	
	@SuppressWarnings("rawtypes")
	public void $refresh() {
		
		InputStream stream = client.capabilities().get(InputStream.class);
		
		try {
			MarshallerPool pool = WFSMarshallerPool.getInstance();
			Unmarshaller unmarshaller = pool.acquireUnmarshaller();

			Object obj = unmarshaller.unmarshal(stream);
			if(obj instanceof JAXBElement){
				obj = ((JAXBElement) obj).getValue();
				capabilities = (WFSCapabilitiesType) obj;
			}
		
		} catch (Exception e) {
			unchecked("cannot read OGC WFS GetCapabilities document", e);
		}
	
	}
}
