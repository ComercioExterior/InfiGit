package models.configuracion.asociar_cuentas;

import com.bdv.infi.dao.CuentasBancoDAO;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que muestra el registro a ser editado
 * @author elaucho
 */
public class AsociarCuentasBDVEdit extends MSCModelExtend{

	
	public void execute() throws Exception {
		
		//DAO a utilizar
		CuentasBancoDAO cuentasBancoDAO = new CuentasBancoDAO(_dso);
		
		//Se lista la transaccion a editar
		cuentasBancoDAO.listarTransaccion(_req.getParameter("transaccion"));
		
		//Se publica el DataSet
		storeDataSet("datos", cuentasBancoDAO.getDataSet());
		
		//Se listan todas las cuentas creadas en configuracion
		cuentasBancoDAO.listarBicBDV("");
		
		//Se publican las cuentas
		storeDataSet("cuentas", cuentasBancoDAO.getDataSet());
		
	}//fin execute
}//fin clase
