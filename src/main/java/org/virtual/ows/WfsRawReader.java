package org.virtual.ows;

import java.io.InputStream;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.virtualrepository.impl.Type;
import org.virtualrepository.ows.WfsFeatureType;
import org.virtualrepository.spi.Importer;

/**
 * WFS Raw reader. This class allows to retrieve WFS FeatureType data as InputStream
 * This reader allows go from codelist (assets) extracted from WFS and present them
 * in tabular form (api)
 * 
 * @author fabiosimeoni
 *
 */
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
