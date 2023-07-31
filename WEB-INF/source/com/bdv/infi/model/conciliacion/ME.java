package com.bdv.infi.model.conciliacion;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.dao.ConciliacionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * @todo Archivo moneda extranjera mediante Infi 
 * 		en la tabla INFI_TB_234_VC_DIVISAS
 * @author nm11383
 *
 */
public class ME extends Thread {

	List<String> lst = new ArrayList<String>();
	
	

	@Override
	public void run() {
//		try {
		DataSource _dso;
		try {
			_dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			consultar(_dso);
			
			
		} catch (Exception e) {
			System.out.println("run() : " +  e.toString());
			e.printStackTrace();
		}
		
		
			
//			lst.add("20210605120162FRWK3N");
//			lst.add("20210605128577KCB60T");
//			lst.add("20210605128412138SZ2");
//			lst.add("20210605122227VUE62D");}
			
//			BCV.sleep(1);
//		} catch (InterruptedException e) {
//			Logger.error(true, "ME : run() " + e);
//		}

	}

	public List<String> Lista() {
		return lst;
	}
	
	public void simularDatos(){
		
		this.lst.add("20210818090010C53V77;17522251;20210818090010C53V7R;10.50;20-08-2021");
		this.lst.add("20210818090010C53V76;17522252;20210818090010C53V8R;10.30;20-08-2021");
		this.lst.add("20210818090010C53V77;17522253;20210818090010C53V9R;10.20;20-08-2021");
		this.lst.add("20210818090010C53V78;17522254;20210818090010C53V6R;1.10;20-08-2021");
		
	}
	
	public void consultar( DataSource dso ){
		System.out.println("llego consulta meee concialicion");
			try {
				ConciliacionDAO conciliacionDAO = new ConciliacionDAO(dso); 
				DataSet _cdao = conciliacionDAO.getDataSet();
				while (_cdao.next()) {
					String cadena = _cdao.getValue("ID_BCV") + ";" + _cdao.getValue("NACIONALIDAD")+_cdao.getValue("NRO_CED_RIF")  + ";0;"  + _cdao.getValue("MTO_DIVISAS") +";"+ _cdao.getValue("FECHA"); 
					System.out.println("cadena ME : " + cadena);
					this.lst.add( cadena );
				}
			} catch (Exception e) {
				System.out.println("llego Exception Consulta");
				simularDatos();
				e.printStackTrace();
			}
	}

}
