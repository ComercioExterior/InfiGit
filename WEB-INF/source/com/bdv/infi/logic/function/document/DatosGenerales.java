package com.bdv.infi.logic.function.document;

import java.util.Map;
import javax.sql.DataSource;

public abstract class DatosGenerales {

	private DataSource dataSource;
	private Map<String, String> mapa;
	
	public DatosGenerales(DataSource ds, Map<String, String> mapa){
		this.dataSource = ds;
		this.mapa = mapa;
	}
	
	public DataSource getDataSource(){
		return this.dataSource;
	}
	
	public Map<String, String> getMapa(){
		return this.mapa;
	}
	
	
}