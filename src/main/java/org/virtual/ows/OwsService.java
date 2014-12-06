package org.virtual.ows;

import static org.virtual.ows.common.Utils.*;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@RequiredArgsConstructor(staticName="service")
@NoArgsConstructor //for json binding
public class OwsService {

	@NonNull @JsonProperty
	private QName name;
	
	@NonNull @JsonProperty
	private String version = "1.0.0";
	
	@NonNull @JsonProperty
	private String uri;
	
	public void uri(String uri) {
		
		validUri("service uri", uri);

		this.uri = uri;
	}
}
