package models.configuracion.cuentasCliente;

import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.logic.interfaces.TipoInstruccion;

import models.msc_utilitys.MSCModelExtend;

public class Filter extends MSCModelExtend {
	public void execute()throws Exception{
		//_req.getSession().removeAttribute("cuentasCliente.filter");
		//CuentasUsoDAO cuentasUsoDAO=new CuentasUsoDAO(_dso);
		GestionPagoDAO gestionPagoDAO=new GestionPagoDAO(_dso);
		//cuentasUsoDAO.listar();
		gestionPagoDAO.listarInstruccion(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
		//gestionPagoDAO.listarInstruccion();
		//storeDataSet("tipos",cuentasUsoDAO.getDataSet());
		storeDataSet("cuenta", gestionPagoDAO.getDataSet());
	}
}
