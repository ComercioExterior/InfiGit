package models.configuracion.grupo_parametros;

import megasoft.*;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;



public class Edit extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{	
		ParametrosDAO parametrosDAO= new ParametrosDAO(_dso);
		
		parametrosDAO.listarParametros(_req.getParameter("PARTIP_NOMBRE_PARAMETRO"),_req.getParameter("pargrp_id"));
		storeDataSet("table", parametrosDAO.getDataSet());
		
		if(_req.getParameter("PARTIP_NOMBRE_PARAMETRO").equalsIgnoreCase( ParametrosSistema.SESION_ACTIVA))
		{
			_config.template = "validar_sesion.htm";
		}
	
	}

}
