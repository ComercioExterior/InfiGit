package models.configuracion.cuentas_bdv;

import com.bdv.infi.dao.CuentasBancoDAO;
import com.bdv.infi.data.CuentasBancoBDV;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que actualiza un registro de la cuenta asociada al BDV SWIFT
 * @author elaucho
 */
public class CuentasBDVUpdate extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a Utilizar
		CuentasBancoDAO cuentasBancoDAO = new CuentasBancoDAO(_dso);
		
		//Objeto cuentas banco a utilizar
		CuentasBancoBDV cuentasBancoBDV = new CuentasBancoBDV();
		cuentasBancoBDV.setIdCuenta(Long.parseLong(_req.getParameter("id_cuenta")));
		cuentasBancoBDV.setCodigoBicBanco(_req.getParameter("codigo_bic"));
		cuentasBancoBDV.setNumeroCuenta(_req.getParameter("cuenta"));
		cuentasBancoBDV.setDescripcion(_req.getParameter("descripcion"));
		cuentasBancoBDV.setUsrUltActualizacion(this.getUserName());		
		
		//Se actualiza el registro en base de datos
		cuentasBancoDAO.actualizar(cuentasBancoBDV);
		
	}//fin execute
}//fin clase
