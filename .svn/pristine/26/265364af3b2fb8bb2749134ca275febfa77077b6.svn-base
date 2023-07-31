package models.configuracion.grupo_parametros;

import megasoft.*;
import com.bdv.infi.dao.GrupoParametrosDAO;


/**Clase encargada de listar todos los grupos de parametros
 */
public class Table extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		//Realizar consulta
		GrupoParametrosDAO grupoParametrosDAO= new GrupoParametrosDAO(_dso);
		grupoParametrosDAO.listarGrupoParametros(_record.getValue("pargrp_nombre"));
		//registrar los datasets exportados por este modelo
		System.out.println("paso grupos");
		storeDataSet("table", grupoParametrosDAO.getDataSet());		
		storeDataSet("datos", grupoParametrosDAO.getTotalRegistros());
		
		
	}
}
