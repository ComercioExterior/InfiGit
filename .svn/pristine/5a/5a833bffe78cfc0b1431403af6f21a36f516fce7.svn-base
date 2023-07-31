package models.configuracion.cuentasCliente;

import com.bdv.infi.dao.CuentasUsoDAO;
import com.bdv.infi.dao.EstadoDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TipoInstruccion;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Addnew extends MSCModelExtend {
	
	public void execute() throws Exception{		
		CuentasUsoDAO cuentasUsoDAO=new CuentasUsoDAO(_dso);
		GestionPagoDAO intruccionDAO= new GestionPagoDAO(_dso);
		EstadoDAO estadoDAO= new EstadoDAO(_dso);
		DataSet _controlCambio=new DataSet();
		
		_controlCambio.append("control_cambio", java.sql.Types.VARCHAR);
		String control_cambio=ParametrosDAO.listarParametros(ParametrosSistema.CONTROL_DE_CAMBIO,_dso);
		_controlCambio.addNew();
		_controlCambio.setValue("control_cambio",control_cambio);
		//Se publica el dataset
		storeDataSet("_controlCambio", _controlCambio);
		
		intruccionDAO.listarInstruccion(String.valueOf(TipoInstruccion.CUENTA_SWIFT));		
		//intruccionDAO.listarInstruccion();
		cuentasUsoDAO.listarUsuoCuenta();
		estadoDAO.consultarEstados();
		
		storeDataSet("UsoCuenta",cuentasUsoDAO.getDataSet());
		storeDataSet("tipoInstruccion", intruccionDAO.getDataSet());
		storeDataSet("estados", estadoDAO.getDataSet());
	}
}
