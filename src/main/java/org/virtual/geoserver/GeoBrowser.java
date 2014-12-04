package org.virtual.geoserver;

import java.util.Collection;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.virtualrepository.AssetType;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

@RequiredArgsConstructor
public class GeoBrowser implements Browser {

	@NonNull
	final GeoClient client;
	
	
	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> types) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
