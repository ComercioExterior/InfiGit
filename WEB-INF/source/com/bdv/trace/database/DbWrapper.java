package com.bdv.trace.database;

import javax.sql.DataSource;
import megasoft.DataSet;
import megasoft.db;

public class DbWrapper {

	
	
	public static DataSource getDataSource(String jdniName) throws Exception
	{
		return db.getDataSource(jdniName);
	}
	public static int exec(DataSource dso, String sql) throws Exception
	{
		return db.exec(dso, sql);
	}
	public static DataSet get(DataSource dso, String sql) throws Exception
	{
		return db.get(dso, sql);
	}
}
