package models.configuracion.grupo_parametros;

import megasoft.AbstractModel;
import com.bdv.infi.dao.GrupoParametrosDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda
 */
public class Filter extends AbstractModel
{
	 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{		
		GrupoParametrosDAO grupoParametrosDAO= new GrupoParametrosDAO(_dso);
		grupoParametrosDAO.listaParametros(ParametrosSistema.FECHAS_CAMPOS_DINAMICOS);
		storeDataSet("grupo_parametros", grupoParametrosDAO.getDataSet());
				
	}	
	

}
