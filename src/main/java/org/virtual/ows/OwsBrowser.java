package org.virtual.ows;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.opengis.ows.v_1_0_0.KeywordsType;
import net.opengis.wfs.v_1_1_0.FeatureTypeType;
import net.opengis.wfs.v_1_1_0.WFSCapabilitiesType;

import org.virtualrepository.AssetType;
import org.virtualrepository.Property;
import org.virtualrepository.ows.WsfFeatureType;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

@RequiredArgsConstructor
public class OwsBrowser implements Browser {

	@NonNull
	final OwsClient client;
	
	
	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> ignore) {
		 
		//as long as we support a single asset type, we can safely ignore the input.
		
		WFSCapabilitiesType capabilities = client.capabilities().get(WFSCapabilitiesType.class);
		
		List<FeatureTypeType> ftypes = capabilities.getFeatureTypeList().getFeatureType();
		
		return ftypes.stream().map(this::adapt).collect(toList());
	}
	
	
	////////////////////////////////////////////////////////////////////////////////// helpers
	
	private WsfFeatureType adapt(FeatureTypeType bean) {
	
		//seems there's no global id and name identifies only in-service.
		//prefix to protec further in vr pool.
		String name = bean.getName().toString();
		String id = client.service.name().toString()+"-"+name;
		
		return new WsfFeatureType(id,name, propsOf(bean));
	}
	
	private List<Property> propsOf(FeatureTypeType bean) {
		
		return props().add("title", bean.getTitle())
					  .add("abstract", bean.getAbstract())
					  .add("keywords", adapt(bean.getKeywords()))
					  .add("srs", bean.getDefaultSRS())
					  .done();
				
	}
	
	private String adapt(List<KeywordsType> kws) {
	
		List<String> flattened = kws.stream().flatMap($->$.getKeyword().stream()).collect(toList()); 
		return flattened.isEmpty() ? null : flattened.toString();
	}
	
	private interface Add {
		
		Add add(String n, String s);
		
		List<Property> done();
	}
	
	private Add props() {
		
		List<Property> list = new ArrayList<>();
		
		return new Add() {
			
			@Override
			public Add add(String name,String val) {
				
				if (val!=null && !val.isEmpty())
					list.add(new Property(name,val));
				
				return this;
			}
			
			@Override
			public List<Property> done() { return list; }
		};
	}
	

	
	

}