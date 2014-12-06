package org.virtual.wfs.configuration;

import java.util.ArrayList;
import java.util.List;

import org.virtual.ows.OwsService;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor(staticName="config")
public class Configuration {

	@NonNull @JsonProperty
	List<OwsService> services = new ArrayList<>();
}
