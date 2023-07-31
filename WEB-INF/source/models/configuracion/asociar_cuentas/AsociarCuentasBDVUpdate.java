package models.configuracion.asociar_cuentas;

import com.bdv.infi.dao.CuentasBancoDAO;
import com.bdv.infi.data.CuentasBancoBDV;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que actualiza un registro en la tabla de asociacion de cuentas a transacciones
 * @author elaucho
 */
public class AsociarCuentasBDVUpdate extends MSCModelExtend{

	
	public void execute() throws Exception {

		//DAO a utilizar
		CuentasBancoDAO cuentasBancoDAO = new CuentasBancoDAO(_dso);
		
		//Objeto data a utilizar
		CuentasBancoBDV cuentasBancoBDV = new CuentasBancoBDV();
		cuentasBancoBDV.setIdCuenta(Long.parseLong(_req.getParameter("id_cuenta")));
		cuentasBancoBDV.setTransaccion(_req.getParameter("transaccion"));
		cuentasBancoBDV.setUsrUltActualizacion(this.getUserName());
		
		//Se actualiza el registro
		cuentasBancoDAO.actualizarRelacion(cuentasBancoBDV);
		
	}//fin execute
}//fin clase
