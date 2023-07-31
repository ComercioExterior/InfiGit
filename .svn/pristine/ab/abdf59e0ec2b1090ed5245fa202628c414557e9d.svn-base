package models.configuracion.cuentas_bdv;

import com.bdv.infi.dao.CuentasBancoDAO;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que muestra el registro a ser editado en las cuentas asociadas al BDv
 * @author elaucho
 */
public class CuentasBDVEdit extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		CuentasBancoDAO cuentasBancoDAO = new CuentasBancoDAO(_dso);
		
		//Se lista la cuenta a ser editada
		cuentasBancoDAO.listarBicBDV(_req.getParameter("id_cuenta"));
		
		//Se publica el DataSet
		storeDataSet("datos", cuentasBancoDAO.getDataSet());
		
	}//fin execute
}//fin clase
