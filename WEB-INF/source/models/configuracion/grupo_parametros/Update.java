package models.configuracion.grupo_parametros;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.GrupoParametros;
import megasoft.AbstractModel;
import megasoft.db;


/**
 * Clase encargada actualizar en Base de Datos un Valor del parametro.
 * 
 */
public class Update extends AbstractModel
{		
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		ParametrosDAO parametrosDAO= new ParametrosDAO(_dso);
		GrupoParametros grupoParametros= new GrupoParametros();
		grupoParametros.setDescripcionParametro(_req.getParameter("partip_descripcion"));
		grupoParametros.setIdParametro(_record.getValue("pargrp_id")); 
		grupoParametros.setNombreParametro(_req.getParameter("partip_nombre_parametro"));
		grupoParametros.setValorParametro(_req.getParameter("parval_valor"));

		String sql= parametrosDAO.modificarParametros(grupoParametros);
		
		db.exec(_dso, sql);
}
}
