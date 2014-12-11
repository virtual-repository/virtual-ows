package org.virtualrepository.ows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.opengis.feature.Feature;

public class Features implements Iterable<Feature> {

	private final List<Feature> features;
	
	public Features(Collection<Feature> features) {
		this.features=new ArrayList<Feature>(features);
	}
	
	public List<Feature> all() {
		return features;
	}
	
	@Override
	public Iterator<Feature> iterator() {
		return features.iterator();
	}
	
	
}
