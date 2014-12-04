package org.virtual.geoserver.configuration;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@RequiredArgsConstructor(staticName="server")
@NoArgsConstructor //for json binding
public class GeoServer {

	@NonNull @JsonProperty
	QName name;
	
	@NonNull @JsonProperty
	String uri;
}
