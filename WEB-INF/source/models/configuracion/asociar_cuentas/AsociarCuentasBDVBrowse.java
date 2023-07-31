package models.configuracion.asociar_cuentas;

import com.bdv.infi.dao.TransaccionDAO;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que lista todas las transacciones
 * @author elaucho
 */
public class AsociarCuentasBDVBrowse extends MSCModelExtend{

	
	public void execute() throws Exception {
		
		//DAO a utilizar
		TransaccionDAO transaccionDAO = new TransaccionDAO(_dso);
		
		//Lista todas las transacciones que tengan o no cuenta asociada
		transaccionDAO.listarTransacciones();
		
		//Se publica el Dataset
		storeDataSet("transacciones", transaccionDAO.getDataSet());
		
	}//fin execute 
}//fin clase
