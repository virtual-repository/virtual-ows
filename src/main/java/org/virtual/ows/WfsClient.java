package org.virtual.ows;

import static java.util.Arrays.*;
import static javax.ws.rs.client.ClientBuilder.*;
import static javax.ws.rs.core.HttpHeaders.*;
import static org.virtual.ows.OwsService.Version.*;
import static org.virtual.ows.common.Utils.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.geotoolkit.feature.xml.jaxb.JAXBFeatureTypeReader;
import org.glassfish.jersey.filter.LoggingFilter;
import org.opengis.feature.FeatureType;


@RequiredArgsConstructor
public class WfsClient {

	final private JAXBFeatureTypeReader reader = new JAXBFeatureTypeReader();
	
	final private Map<String,List<? extends FeatureType>> map = new HashMap<>();
	
	@NonNull
	final OwsService service;

	public void ping() {
		client().target(service.uri()).request().head();
	}
	
	public Builder capabilities() {
		return make("GetCapabilities").request();
	}
	
	public Builder featuresFor(@NonNull String featureType) {
		
		String nameClause = service.version().before(v200) ? "typeName" : "typeNames";
		
		return make("GetFeature").queryParam(nameClause,featureType).request();
	}

	
	public List<? extends FeatureType> typesFor(@NonNull String name)  {
		
		List<? extends FeatureType> types = map.get(name);
		
		if (types==null) {
		
			try {
				String schema = describe(name).get(String.class);
			
				types = reader.read(schema);
			}
			catch(Exception e) {
				throw unchecked("cannot acquire schema for "+name,e);
			}
			
			map.put(name, types);
		}
		
		return types;
	}

	
	////////////////////////////////////////////////////////////////////////////// helpers
	
	
	
	private Builder describe(@NonNull String featureType) {
		
		String nameClause = service.version().before(v200) ? "typeName" : "typeNames";
		
		return make("DescribeFeatureType").queryParam(nameClause,featureType).request();
	}
	
	
	
	private  WebTarget make(String $) {
		
		return client().target(service.uri())
				.queryParam("service","wfs")
				.queryParam("version",service.version().value())
				.queryParam("request", $);
	}
	
	private Client client() { 
		
		return newClient()
			.register(ContentTypeFixer.instance)
			.register(new LoggingFilter(Logger.getLogger(LoggingFilter.class.getName()),true))
			//.register(JAXBContextProvider.instance)
			;
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
	
	
	@Provider
	@Slf4j
	public static class JAXBContextProvider implements ContextResolver<JAXBContext> {
	    
		static final JAXBContextProvider instance = new JAXBContextProvider();
		
		private JAXBContext context;
	 
	    public JAXBContext getContext(Class<?> type) {
	      
	    	if (context == null) {
	            try {
	                context = JAXBContext.newInstance("net.opengis.wfs.v_1_1_0:net.opengis.gml.v_3_1_1");
	            } catch (JAXBException e) {
	                log.error("cannot use jaxb provider");
	            }
	        }
	 
	        return context;
	    }
	}
}