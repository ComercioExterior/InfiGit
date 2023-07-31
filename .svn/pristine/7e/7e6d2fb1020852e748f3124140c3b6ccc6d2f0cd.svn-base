package models.configuracion.cuentas_bdv;

import com.bdv.infi.dao.CuentasBancoDAO;
import models.msc_utilitys.MSCModelExtend;

/**
 * Modelo relacionado al browse para las cuentas BDV Configuración-Cuentas_BDV
 * @author elaucho
 */
public class CuentasBDVBrowse extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//Objeto DAO a utilizar
		CuentasBancoDAO cuentasBancoDAO = new CuentasBancoDAO(_dso);
		
		//lista todas las cuentas del banco de venezuela
		cuentasBancoDAO.listarBicBDV("");
		
		//Store del Dataset
		storeDataSet("cuentas", cuentasBancoDAO.getDataSet());
	}//fin execute

}//fin clase
