package org.virtual.ows.profile;

import static org.virtual.ows.common.Utils.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.xml.MarshallerPool;
import org.geotoolkit.wfs.xml.WFSMarshallerPool;
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
				
				new Property("title",capabilities.getService().getTitle()),
				new Property("abstract",capabilities.getService().getAbstract()),
				new Property("keywords",capabilities.getService().getKeywords()),		
				new Property("type", capabilities.getService().getName()),
				new Property("version",capabilities.getVersion())
					
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
	
	
	@SuppressWarnings("rawtypes")
	public void $refresh() {

		InputStream stream = client.capabilities().get(InputStream.class);
		
		try {
			MarshallerPool pool = WFSMarshallerPool.getInstanceV100();
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
