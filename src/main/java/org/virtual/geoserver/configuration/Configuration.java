package org.virtual.geoserver.configuration;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor(staticName="config")
public class Configuration {

	@NonNull @JsonProperty
	List<GeoServer> servers = new ArrayList<>();
}
