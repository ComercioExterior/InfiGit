package models.gestion_pago_cheque;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que elimina un registro de un dataset  que se encuentra en sesion
 * @author elaucho
 */
public class GestionPagoEliminarSesion extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		String row = _req.getParameter("row");
		
/*
 * Se remueve la fila del dataset de sesion
 */
		DataSet _intrucciones = (DataSet)_req.getSession().getAttribute("infi.banco.instrucciones");
		_intrucciones.first();
		while(_intrucciones.next()){
			if(_intrucciones.getValue("_row").equalsIgnoreCase(row)){
				_intrucciones.removeRow(Integer.parseInt(_intrucciones.getValue("_row")));
			}//fin if
		}//fin while
		_req.getSession().setAttribute("infi.banco.instrucciones", _intrucciones);
	}//fin execute
}//fin clase
