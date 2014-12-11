package org.virtual.ows;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;

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
		
		JAXPStreamFeatureReader reader = new JAXPStreamFeatureReader((List) client.typesFor(name));
		
		Response response =  client.featuresFor(asset.name()).get();
		
		Collection coll = (Collection) reader.read(response.getEntity());
		
		return new Features(coll); 
		
	}

}
