package org.virtual.ows;

import java.io.InputStream;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.virtualrepository.impl.Type;
import org.virtualrepository.ows.WfsFeatureType;
import org.virtualrepository.spi.Importer;

//this is a reader from codelist (assets) extracted from geoservers and presented in tabular form (api)
@RequiredArgsConstructor
public class WfsRawReader implements Importer<WfsFeatureType, InputStream> {

	@NonNull
	final WfsClient client; 
	
	@Override
	public Type<? extends WfsFeatureType> type() {
		return WfsFeatureType.type;
	}

	@Override
	public Class<InputStream> api() {
		return InputStream.class;
	}

	@Override
	public InputStream retrieve(WfsFeatureType asset) throws Exception {
		
		return client.featuresFor(asset.name()).get(InputStream.class);
	}


}
