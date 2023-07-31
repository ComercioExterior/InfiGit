package models.configuracion.cuentas_bdv;

import com.bdv.infi.dao.CuentasBancoDAO;
import com.bdv.infi.data.CuentasBancoBDV;

import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que inserta un registro en la tabla de cuentas 007
 * @author elaucho
 */
public class CuentasBDVInsert extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		//objeto DAO a utilizar
		CuentasBancoDAO cuentasBancoDAO = new CuentasBancoDAO(_dso);
		
		//Objeto DATA
		CuentasBancoBDV cuentasBancoBDV = new CuentasBancoBDV();
		cuentasBancoBDV.setCodigoBicBanco(_req.getParameter("codigo_bic"));
		cuentasBancoBDV.setNumeroCuenta( _req.getParameter("cuenta"));
		cuentasBancoBDV.setDescripcion(_req.getParameter("descripcion"));
		cuentasBancoBDV.setUsrUltActualizacion(this.getUserName());		
		
		//Insertamos el registro
		cuentasBancoDAO.insertarCuenta(cuentasBancoBDV);
		
	}//fin execute
}//fin clase
