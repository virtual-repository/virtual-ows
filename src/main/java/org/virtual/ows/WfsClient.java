package org.virtual.ows;

import static java.util.Arrays.*;
import static javax.ws.rs.client.ClientBuilder.*;
import static javax.ws.rs.core.HttpHeaders.*;
import static org.virtual.ows.OwsService.Version.*;
import static org.virtual.ows.common.Utils.*;
import static org.virtual.wfs.configuration.Configuration.Mode.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Priority;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.geotoolkit.feature.type.GeometryType;
import org.geotoolkit.feature.type.PropertyDescriptor;
import org.geotoolkit.feature.xml.jaxb.JAXBFeatureTypeReader;
import org.glassfish.jersey.client.filter.EncodingFilter;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.message.GZipEncoder;
import org.opengis.feature.FeatureType;
import org.virtual.wfs.configuration.Configuration.Mode;

/**
 * Simple WFS Client based on the OGC official GeoAPI Feature interface
 * and GeoToolkit implementation.
 * 
 * - Currently operational for WFS version 1.0.0 and 1.1.0
 * - WFS 2.0.0 is not supported yet
 * 
 * @author eblondel
 *
 */
@SuppressWarnings("deprecation")
@Slf4j
@RequiredArgsConstructor 
public class WfsClient {
	
	final private JAXBFeatureTypeReader reader = new JAXBFeatureTypeReader();
	
	final private Map<String,List<? extends FeatureType>> map = new HashMap<>();
	

	@Getter @Setter
	private Mode mode;
	
	@NonNull @Getter
	final private OwsService service;
	

	
	public void ping() {
		client(false).target(service.uri()).request().head();
	}
	
	
	public Builder capabilities() {
		
		log.info("fetching {}'s capabilities",service.name());
		
		return make("GetCapabilities",false).request();
	
	}
	

	public Builder featuresFor(@NonNull String typename) {
		
		String nameClause = service.version().before(v200) ? "typeName" : "typeNames";
		
		WebTarget target =  make("GetFeature", service.compress()).queryParam(nameClause,typename);
		
		if(service.excludeGeom() || service.excludes().size() > 0){
		
			StringBuilder builder = new StringBuilder();
			
			for (String prop : propertiesFor(typename))
				builder.append(builder.length()>0? ","+prop : prop);
			
			if (builder.length()>0)
				target = target.queryParam("propertyName", builder.toString());
		}

		return target.request();
		
	}

	
	public FeatureType typeFor(@NonNull String name)  {
		
		List<? extends FeatureType> types = map.get(name);
		
		if (types==null) {
		
			try {
				
				String schema = describe(name).get(String.class);
			
				types = reader.read(schema);
				
			}
			catch(Exception e) {
				throw unchecked("cannot acquire schemas for "+name,e);
			}
			
			map.put(name, types);
		}
		
		FeatureType type = types.stream()
				.filter(f -> (name.contains(f.getName().toString())
						   || name.contains(f.getName().tip().toString())))
				.collect(Collectors.<FeatureType>toList()).get(0);
		
		return type;
	}

	
	////////////////////////////////////////////////////////////////////////////// helpers
	
	
	@SuppressWarnings("deprecation")
	private Set<String> propertiesFor(String typename) {
		
		Set<String> props = new HashSet<String>();
		
		org.geotoolkit.feature.type.FeatureType type = (org.geotoolkit.feature.type.FeatureType) typeFor(typename);

		for(PropertyDescriptor descriptor : type.getDescriptors()) {
			
			String ns = descriptor.getName().head().toString();
			String name = descriptor.getName().tip().toString();
			
			if(service.excludeGeom()){
				if(!(descriptor.getType() instanceof GeometryType)
				&& !service.excludes().contains(name)
				&& !name.startsWith("@")
				&& !ns.contains("gml"))
						props.add(name);
			}else{
				if (!service.excludes().contains(name)
					&& !name.startsWith("@")
					&& !name.equals("name")
					&& !ns.contains("gml"))
					props.add(name);
			}
		}
		
		return props;
	}

	private Builder describe(@NonNull String typename) {
		
		String nameClause = service.version().before(v200) ? "typeName" : "typeNames";
		
		return make("DescribeFeatureType", false).queryParam(nameClause,typename).request();
	}
	
	private  WebTarget make(String $, boolean compressResponse) {
		
		return client(compressResponse)
				.target(service.uri())
				.queryParam("service","wfs")
				.queryParam("version",service.version().value())
				.queryParam("request", $);
	}
	
	private Client client(boolean compressResponse) { 
		
		Client client = newClient()
				.register(ContentTypeFixer.instance);
		
		if(compressResponse){
			client
				.register(GZipEncoder.class)
				.register(EncodingFilter.class);
		}
		
		Logger.getLogger("org.geotoolkit.feature.xml").setLevel(Level.OFF);
		if (mode==development) {
			client.register(new LoggingFilter(Logger.getLogger(LoggingFilter.class.getName()),true));
			Logger.getLogger("org.geotoolkit.feature.xml").setLevel(Level.INFO);
		}
		
		return client;
	}
	
	
	//fixes ogc illegal content subtype
	@Priority(Integer.MIN_VALUE)
	private static class ContentTypeFixer implements ClientResponseFilter  {
		
		static final ContentTypeFixer instance = new ContentTypeFixer();
		
		@Override
		public void filter(ClientRequestContext requestContext,
				ClientResponseContext responseContext) throws IOException {
			
			if (responseContext.getHeaderString(CONTENT_TYPE).contains("subtype=gml"))
				responseContext.getHeaders().put(CONTENT_TYPE, asList("text/xml"));
			
		}
	}
}
