package models.carga_inicial_pagos;

import com.bdv.infi.dao.CargaInicialPagoDAO;

import megasoft.*;import models.msc_utilitys.*;

public class BrowseRegistrosCargados extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _dsBuenos = null;	
	private DataSet _dsMalos = null;	
	private DataSet _dsBuenosNoRegs = null;
	private DataSet _datos = null;
	private CargaInicialPagoDAO cargaInicialPagoDAO =null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		cargaInicialPagoDAO = new CargaInicialPagoDAO(_dso); 
		_datos = new DataSet();
		_datos.append("t_registros_buenos", java.sql.Types.VARCHAR);
		_datos.append("t_registros_malos", java.sql.Types.VARCHAR);
		_datos.append("t_registros_buenos_no_regs", java.sql.Types.VARCHAR);
		_datos.addNew();
			
		//----Buscar registros buenos----------------------------
		cargaInicialPagoDAO.consultarCargaInicialPagos(false, true, _record.getValue("titulo_id"));
		_dsBuenos=cargaInicialPagoDAO.getDataSet();
		
		//-----Buscar registros malos-----------------------------
		cargaInicialPagoDAO.consultarCargaInicialPagos(true, false, _record.getValue("titulo_id"));
		_dsMalos=cargaInicialPagoDAO.getDataSet();	
		
		//-----Buscar registros buenos no registrados--------------
		cargaInicialPagoDAO.consultarCargaInicialPagos(false, false, _record.getValue("titulo_id"));
		_dsBuenosNoRegs=cargaInicialPagoDAO.getDataSet();	
		
		_datos.setValue("t_registros_buenos", String.valueOf(_dsBuenos.count()));
		_datos.setValue("t_registros_malos", String.valueOf(_dsMalos.count()));
		_datos.setValue("t_registros_buenos_no_regs", String.valueOf(_dsBuenosNoRegs.count()));
		
		//Remover registros de sesion utilizados en la clase de procesamiento
		_req.getSession().removeAttribute("registrosTemp");
		_req.getSession().removeAttribute("ordenes");		
	
		storeDataSet( "table_buenos", _dsBuenos );
		storeDataSet( "table_malos", _dsMalos );
		storeDataSet( "table_buenos_no_regs", _dsBuenosNoRegs );
		storeDataSet( "datos", _datos );
		storeDataSet( "record", _record);
	}
}