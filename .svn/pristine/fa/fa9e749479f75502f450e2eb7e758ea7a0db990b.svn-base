package models.configuracion.cuentas_bdv;

import com.bdv.infi.dao.CuentasBancoDAO;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que elimina un registro de cuentas asociadas al BDV
 * @author elaucho
 */
public class CuentasBDVDelete extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		//objeto DAO a utilizar
		CuentasBancoDAO cuentasBancoDAO = new CuentasBancoDAO(_dso);
		
		//Insertamos el registro
		cuentasBancoDAO.eliminarCuenta(Long.parseLong(_req.getParameter("id_cuenta")));
		
	}//fin execute
}//fin clase
