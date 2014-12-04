package org.virtual.geoserver;

import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.tabular.Table;

public class GeoReader implements Importer<CsvCodelist, Table> {

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
		// TODO Auto-generated method stub
		return null;
	}

}
