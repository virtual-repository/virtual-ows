package org.virtualrepository.ows;

import org.virtualrepository.Property;
import org.virtualrepository.comet.CometAsset;
import org.virtualrepository.impl.AbstractAsset;
import org.virtualrepository.impl.AbstractType;
import org.virtualrepository.impl.Type;

public class WfsFeatureType extends AbstractAsset {
	
	public static final Type<WfsFeatureType> type = new AbstractType<WfsFeatureType>( "wfs/feature") {};
	
	
	public <T extends CometAsset> WfsFeatureType(String id, String name, Property ... properties) {
		
		super(type,id,name,properties);

	}
	
	
}
