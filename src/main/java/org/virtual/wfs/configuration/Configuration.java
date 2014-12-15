package org.virtual.wfs.configuration;

import static org.virtual.wfs.configuration.Configuration.Mode.*;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.virtual.ows.OwsService;

import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@RequiredArgsConstructor(staticName="config")
public class Configuration {

	public static enum Mode { development, production; }
	
	@JsonProperty
	Mode mode = production;
	
	@NonNull @JsonProperty
	List<OwsService> services = new ArrayList<>();
}
