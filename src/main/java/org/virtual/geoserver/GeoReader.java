package org.virtual.geoserver;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.tabular.Table;

//this is a reader from codelist (assets) extracted from geoservers and presented in tabular form (api)
@RequiredArgsConstructor
public class GeoReader implements Importer<CsvCodelist, Table> {

	@NonNull
	final GeoClient client; 
	
	@Override
	public Type<? extends CsvCodelist> type() {
		return CsvCodelist.type;
	}

	@Override
	public Class<Table> api() {
		return Table.class;
	}

	@Override
	public Table retrieve(CsvCodelist asset) throws Exception {
		
		// TODO: actually retrieve the codelist described in input
		return null;
	}

}
