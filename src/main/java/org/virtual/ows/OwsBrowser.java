package org.virtual.ows;

import static java.util.stream.Collectors.*;

import java.util.Collection;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.virtual.ows.profile.WfsProfile;
import org.virtual.ows.profile.WfsTypeProfile;
import org.virtualrepository.AssetType;
import org.virtualrepository.ows.WfsFeatureType;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

@RequiredArgsConstructor
public class OwsBrowser implements Browser {

	@NonNull
	final WfsProfile profile;
	
	@NonNull
	final WfsClient client;
	
	
	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> ignore) {
		 
		//as long as we support a single asset type, we can safely ignore the input.
		
		 if (profile.types().isEmpty())
			 profile.refresh();
		 
		return profile.types().stream().map(this::adapt).collect(toList());
	}
	
	
	////////////////////////////////////////////////////////////////////////////////// helpers
	
	private WfsFeatureType adapt(WfsTypeProfile type) {
		
		String id = client.service().name()+"-"+type.name();
	
		return new WfsFeatureType(id, type.name(), type.properties().toArray());
	}
	
}
