package org.virtual.ows;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.geotoolkit.feature.xml.jaxp.JAXPStreamFeatureReader;
import org.virtualrepository.impl.Type;
import org.virtualrepository.ows.Features;
import org.virtualrepository.ows.WfsFeatureType;
import org.virtualrepository.spi.Importer;

//this is a reader from codelist (assets) extracted from geoservers and presented in tabular form (api)
@RequiredArgsConstructor
public class WfsReader implements Importer<WfsFeatureType,Features> {

	@NonNull
	final WfsClient client; 
	
	@Override
	public Type<? extends WfsFeatureType> type() {
		return WfsFeatureType.type;
	}

	@Override
	public Class<Features> api() {
		return Features.class;
	}

	@Override
	@SuppressWarnings("all")
	public Features retrieve(@NonNull WfsFeatureType asset) throws Exception {
	
		String name = asset.name();
		
		JAXPStreamFeatureReader reader = new JAXPStreamFeatureReader((List) Arrays.asList(client.typeFor(name)));
		
		InputStream stream =  client.featuresFor(asset.name()).get(InputStream.class);

		Collection coll = (Collection) reader.read(stream);
		
		return new Features(coll);
		
	}

}
