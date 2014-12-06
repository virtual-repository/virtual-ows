package org.virtual.ows;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.virtualrepository.impl.Type;
import org.virtualrepository.ows.WsfFeatureType;
import org.virtualrepository.spi.Importer;

//this is a reader from codelist (assets) extracted from geoservers and presented in tabular form (api)
@RequiredArgsConstructor
public class WfsReader implements Importer<WsfFeatureType, String> {

	@NonNull
	final OwsClient client; 
	
	@Override
	public Type<? extends WsfFeatureType> type() {
		return WsfFeatureType.type;
	}

	@Override
	public Class<String> api() {
		return String.class;
	}

	@Override
	public String retrieve(WsfFeatureType asset) throws Exception {
		
		// TODO: actually retrieve the codelist described in input
		return null;
	}

}
