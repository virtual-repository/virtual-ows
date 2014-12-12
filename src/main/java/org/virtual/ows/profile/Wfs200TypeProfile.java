package org.virtual.ows.profile;

import static java.util.stream.Collectors.*;
import static org.virtual.ows.common.Utils.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.geotoolkit.wfs.xml.v200.FeatureTypeType;
import org.virtualrepository.Properties;

@RequiredArgsConstructor
public class Wfs200TypeProfile implements WfsTypeProfile {

	@NonNull
	final private FeatureTypeType type;
	
	@Override
	public String name() {
		return type.getName().getPrefix()+":"+type.getName().getLocalPart();
	}

	@Override
	public Properties properties() {
		
		Properties properties = new Properties();
		
		PropertyBuilder builder = build(properties);
		
		type.getTitle().forEach($->builder.add("title",$.getValue()));
		type.getAbstract().forEach($->builder.add("abstract", $.getValue()));

		builder
			.add("keywords", type.getKeywords().stream().flatMap($->$.getKeywordList().stream()).collect(toList()).toString())
			.add("default CRS", type.getDefaultCRS())
			.add("no CRS", type.getNoCRS().toString())
		;
		
		return properties;
	}

	
}
