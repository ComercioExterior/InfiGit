package models.configuracion.cuentasCliente;

import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import models.msc_utilitys.MSCModelExtend;


/**
 * 
 * @author bag
 *
 */
public class TipoCuentaAjax extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
//codigo de Ajax a ejecutar

		String cuenta_uso		= _req.getParameter("cuenta_uso");

	
		GestionPagoDAO tipoCuentaDAO = new GestionPagoDAO(_dso);
	/*
	 * Si la cuenta de uso es igual a pago de capital, pago cupones, pago bono se listaran los tipos de cuenta (swift, nacional,operacion de cambio,cheque)
	 */
		if(cuenta_uso.equalsIgnoreCase(UsoCuentas.PAGO_DE_CAPITAL)||cuenta_uso.equalsIgnoreCase(UsoCuentas.PAGO_DE_CUPONES)) ;
		{
					
			tipoCuentaDAO.listarInstruccion(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
	
		}
		/**
		 * si la cuenta de uso es igual a pago de comisiones se lista solo el tipo de cuenta (nacional)
		 */

		 if(cuenta_uso.equalsIgnoreCase(UsoCuentas.COBRO_DE_COMISIONES))
		{		
			tipoCuentaDAO.listarCobroComision();
			
		}
		 storeDataSet("datos", tipoCuentaDAO.getDataSet());	
	}//fin execute

}//Fin ajaxTipoCuenta
