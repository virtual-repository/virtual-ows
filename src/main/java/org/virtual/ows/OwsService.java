package org.virtual.ows;

import static org.virtual.ows.OwsService.Version.*;
import static org.virtual.ows.common.Utils.*;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@Data
@RequiredArgsConstructor(staticName="service")
@NoArgsConstructor //for json binding
public class OwsService {
	
	public static enum Version {
		
		v100("1.0.0"),v110("1.1.0"),v200("2.0.0");
		
		private final String value;
		
		Version(String value) {
			this.value=value;
		}
		
		@JsonValue
		public String value() {
			return value;
		}
		
		public boolean before(Version v) {
			return this.ordinal()<v.ordinal();
		}
		
	}

	@NonNull @JsonProperty
	private QName name;
	
	@NonNull @JsonProperty
	private Version version = v100;
	
	@NonNull @JsonProperty
	private String uri;
	
	@JsonProperty
	private boolean compress = true;
	
	@JsonProperty
	private Set<String> excludes = new HashSet<>();
	
	public void uri(String uri) {
		
		validUri("service uri", uri);

		this.uri = uri;
	}
	
	


}
