package models.cierre_sistema.activacion_cierre;

import megasoft.AbstractModel;
import com.bdv.infi.dao.CierreSistemaDAO;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda de la Consulta de Titulos en Custodia de los Clientes
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class Browse extends AbstractModel {
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
	
		CierreSistemaDAO cierreSistemaDAO=new CierreSistemaDAO(_dso);
		cierreSistemaDAO.consultaCierreSistema();
		
		//System.out.println("FECHA SISTEMA --------> " + _fechaSistema);
		storeDataSet("cierre_sistema",cierreSistemaDAO.getDataSet());
	}
		
}
