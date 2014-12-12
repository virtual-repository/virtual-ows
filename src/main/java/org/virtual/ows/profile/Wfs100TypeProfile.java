package org.virtual.ows.profile;

import static org.virtual.ows.common.Utils.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.geotoolkit.wfs.xml.v100.FeatureTypeType;
import org.virtualrepository.Properties;

@RequiredArgsConstructor
public class Wfs100TypeProfile implements WfsTypeProfile {

	@NonNull
	final private FeatureTypeType type;
	
	@Override
	public String name() {
		return type.getName().getPrefix()+":"+type.getName().getLocalPart();
	}

	@Override
	public Properties properties() {
		
		Properties properties = new Properties();
		
		build(properties)
				.add("title", type.getTitle())
				.add("abstract", type.getAbstract())
				.add("keywords", type.getKeywords())
				.add("default CRS", type.getDefaultCRS())
		;
		
		return properties;
	}
	
}
