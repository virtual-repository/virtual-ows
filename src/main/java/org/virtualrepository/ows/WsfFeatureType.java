package org.virtualrepository.ows;

import java.util.List;

import org.virtualrepository.Property;
import org.virtualrepository.comet.CometAsset;
import org.virtualrepository.impl.AbstractAsset;
import org.virtualrepository.impl.AbstractType;
import org.virtualrepository.impl.Type;

public class WsfFeatureType extends AbstractAsset {
	
	public static final Type<WsfFeatureType> type = new AbstractType<WsfFeatureType>( "wfs/feature") {};
	
	
	public <T extends CometAsset> WsfFeatureType(String id, String name, List<Property> properties) {
		
		super(type,id,name,properties.toArray(new Property[0]));

	}
	
	
}
