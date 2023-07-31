package models.cierre_sistema.reprocesamiento_cierre;

import megasoft.AbstractModel;
import com.bdv.infi.dao.CierreSistemaDAO;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda de la Consulta de Titulos en Custodia de los Clientes
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class Filter extends AbstractModel {

	private CierreSistemaDAO cierreSistemaDAO=null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		cierreSistemaDAO=new CierreSistemaDAO(_dso);		
		cierreSistemaDAO.consultaCierreSistema();
		
		storeDataSet("reproceso_cierre", cierreSistemaDAO.getDataSet());
	}
	
}
