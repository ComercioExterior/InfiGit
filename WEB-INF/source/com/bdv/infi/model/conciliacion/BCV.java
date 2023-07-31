package com.bdv.infi.model.conciliacion;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.Logger;
import megasoft.db;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.model.menudeo.Movimientos;

/**
 * @todo Archivo de moneda mediante un XML(WEB SERVICE) del Banco Central de Venezuela, https
 * 
 * @author nm11383
 * 
 */
public class BCV extends Thread {

	List<String> lst = new ArrayList<String>();
	protected HashMap<String, String> parametrosMenudeBCV;

	@Override
	public void run() {
		try {
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			obtenerParametros(dso);
			Movimientos mvts = new Movimientos();
			System.out.println("parametrosMenudeBCV.get(ConstantesGenerales.FECHA_LECTURA) : "+ parametrosMenudeBCV.get(ConstantesGenerales.FECHA_LECTURA));
			mvts.EjecutarMovimiento(parametrosMenudeBCV.get(ConstantesGenerales.FECHA_LECTURA));
			Method Fn = Movimientos.class.getMethod("lecturaMovimientoBcvBeta");
			mvts.procesar(mvts, Fn);
			lst = mvts.ListarClienteBeta();
//			simularDatos();
			BCV.sleep(1);
		} catch (InterruptedException e) {
			Logger.error(true, "BCV : run() InterruptedException" + e);
			
		} catch (Exception e) {
			Logger.error(true, "BCV : run() " + e);
			System.out.println("BCV : run() " + e);
			System.out.println("llego Exception");
			simularDatos();
			
		}
	}

	protected void obtenerParametros(DataSource _dso) {
		try {
			ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
			parametrosMenudeBCV = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MENUDEO);
		} catch (Exception e) {
			Logger.error(true, "BCV : obtenerParametros() " + e);
		}

	}

	public List<String> Lista() {
		return this.lst;
	}
	
	public void simularDatos(){
		this.lst.add("20210818090010C53V77;17522251;20210818090010C53V7R;10.50;20-08-2021");
		this.lst.add("20210818090010C53V76;17522252;20210818090010C53V8R;10.30;20-08-2021");
		this.lst.add("20210818090010C53V7X;17522253;20210818090010C53V9R;10.20;20-08-2021");
		this.lst.add("20210818090010C53V7L;17522254;20210818090010C53V6R;1.10;20-08-2021");;
//		this.lst.add("22045795");
//		this.lst.add("22045796");
//		this.lst.add("22045797");
//		this.lst.add("22045798");
//		this.lst.add("22045799");
//		this.lst.add("22045710");
//		this.lst.add("22045713");
		
	}

}
